package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.InternalServerError;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.order.client.PaymentClient;
import fun.hercules.order.order.platform.order.dto.DeferredNotifyRequest;
import fun.hercules.order.order.platform.order.dto.DeferredTransaction;
import fun.hercules.order.order.platform.order.dto.OrderBillDTO;
import fun.hercules.order.order.platform.order.dto.UploadExcelResponse;
import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.model.PayMethod;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentTransaction;
import fun.hercules.order.order.platform.order.utils.ChargeStatusMapping;
import fun.hercules.order.order.platform.order.utils.ExcelHeaderMap;
import fun.hercules.order.order.platform.order.utils.ExcelUtil;
import fun.hercules.order.order.platform.order.utils.ReflectUtil;
import fun.hercules.order.order.platform.order.validators.OrderBillImportationValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
public class OrderBillExcelService {
    @Value("${importation.order-bill.max-lines:10000}")
    private int maxImportationLines;

    private final OrderBillService orderBillService;
    private final PaymentService paymentService;
    private final PaymentClient paymentClient;
    private final OrderService orderService;
    private final PaymentTransactionService paymentTransactionService;
    private final OrderBillImportationValidator importationValidator;


    public OrderBillExcelService(OrderBillService orderBillService,
                                 PaymentService paymentService,
                                 PaymentClient paymentClient,
                                 OrderService orderService,
                                 PaymentTransactionService paymentTransactionService,
                                 OrderBillImportationValidator importationValidator) {
        this.orderBillService = orderBillService;
        this.paymentService = paymentService;
        this.paymentClient = paymentClient;
        this.orderService = orderService;
        this.paymentTransactionService = paymentTransactionService;
        this.importationValidator = importationValidator;
    }


    public List<Map<String, Object>> getExcelBodyList(@RequestParam("fileUpload") MultipartFile multipartFile) {
        Workbook workbook = ExcelUtil.checkExcelFormat(multipartFile);
        List<String> headerKeys = ExcelHeaderMap.getHeaderKeys(ExcelUtil.parseExcelHeader(workbook));
        return ExcelUtil.parseExcelBody(workbook, headerKeys, 1, maxImportationLines);
    }

    public void writeExcel(HttpServletResponse response, List<String> headerKeys, List<OrderBillDTO> orderBills) {
        XSSFWorkbook workbook = new ExcelUtil().createExcel("对账单", ExcelHeaderMap.getHeaderValues(headerKeys), getExcelBody(headerKeys, orderBills));
        OutputStream os;
        try {
            setFilename(response, "对账单_" + System.currentTimeMillis() + ".xlsx");
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new InternalServerError(ErrorCode.SERVER_ERROR, "meet error when download excel data");

        }
    }

    private void setFilename(HttpServletResponse response, String filename) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + new String(filename.getBytes("utf-8"), "iso-8859-1"));
    }

    private List<List<Object>> getExcelBody(List<String> headerKeys, List<OrderBillDTO> orderBills) {
        return orderBills.stream().map(orderBill -> {
            List<Object> body = new ArrayList<>();
            headerKeys.forEach(
                    h -> body.add(ReflectUtil.getFieldValueByFieldName(h, orderBill))
            );
            return body;
        }).collect(toList());
    }

    @Transactional
    public List<OrderBill> updateOrderBills(@NotNull List<Map<String, Object>> bodyList, List<OrderBill> orderBillsInDb) {
        Map<String, OrderBill> orderBillsInDbMap = orderBillsInDb.stream().collect(Collectors.toMap(OrderBill::getOrderId, Function.identity()));
        List<OrderBill> toUpdateBills = modifyOrderBills(bodyList, orderBillsInDbMap);
        updatePayment(orderBillsInDb);
        return orderBillService.save(toUpdateBills);
    }

    private List<OrderBill> modifyOrderBills(List<Map<String, Object>> bodyList, Map<String, OrderBill> orderBillsInDbMap) {
        return bodyList.stream()
                .map(item -> {
                    OrderBill bill = orderBillsInDbMap.get(String.valueOf(item.get("orderId")));
                    bill.setProductChargeSettled(ChargeStatusMapping.fromProductChargeStatus(String.valueOf(item.get("productChargeStatus"))));
                    bill.setServiceChargeSettled(ChargeStatusMapping.fromServiceChargeStatus(String.valueOf(item.get("serviceChargeStatus")), bill.getPayChannel()));
                    bill.setCommissionChargeSettled(ChargeStatusMapping.fromCommissionChargeStatus(String.valueOf(item.get("commissionChargeStatus")), bill.getPayChannel()));
                    return bill;
                })
                .collect(toList());
    }

    private void updatePayment(List<OrderBill> orderBillsInDb) {
        List<OrderBill> defermentOrderBills = orderBillsInDb
                .stream()
                .filter(orderBill -> orderBill.getPayMethod().equals(PayMethod.DEFERMENT))
                .collect(toList());

        if (CollectionUtils.isNotEmpty(defermentOrderBills)) {
            handleDefermentData(defermentOrderBills);
        }
    }

    private void handleDefermentData(List<OrderBill> orderBills) {
        List<OrderBill> needUpdatePaymentBills = getNeedUpdatedDefermentOrderBills(orderBills);
        Map<String, Payment> paymentMap = getPaymentByOrderIds(needUpdatePaymentBills);
        log.info("To update payment : {} ", paymentMap);
        if (MapUtils.isNotEmpty(paymentMap)) {
            DeferredNotifyRequest request = DeferredNotifyRequest.builder().paymentIds(new ArrayList<>(paymentMap.keySet())).build();
            List<DeferredTransaction> deferredTransactions = paymentClient.updateDeferredPayTransStatus(request);
            List<BusinessOrder> businessOrders = listBusinessOrderList(needUpdatePaymentBills);
            log.info("Find business orders : {} .", businessOrders.stream().map(BusinessOrder::getId).collect(toList()));
            paymentService.createPaymentTransactions(deferredTransactions, businessOrders, paymentMap);
        }
    }

    private List<BusinessOrder> listBusinessOrderList(List<OrderBill> needUpdatePaymentBills) {
        Map<String, Set<String>> groupByOrderType = needUpdatePaymentBills
                .stream()
                .collect(groupingBy(OrderBill::getOrderType, mapping(OrderBill::getOrderId, toSet())));

        return groupByOrderType.entrySet()
                .stream()
                .flatMap(entry -> orderService.listOrderByTypeAndIds(entry.getKey(), entry.getValue()).stream())
                .collect(toList());
    }

    private Map<String, Payment> getPaymentByOrderIds(List<OrderBill> needUpdatePaymentBills) {
        List<String> orderIds = needUpdatePaymentBills.stream().map(OrderBill::getOrderId).collect(Collectors.toList());
        return paymentService.findByOrderIds(orderIds).stream().collect(toMap(Payment::getId, Function.identity()));
    }

    private List<OrderBill> getNeedUpdatedDefermentOrderBills(List<OrderBill> orderBills) {
        List<String> orderIdList = orderBills.stream().map(OrderBill::getOrderId).collect(toList());
        Set<String> haveCreatedTransactionOrderIds = paymentTransactionService.findByOrderIds(orderIdList)
                .stream()
                .map(PaymentTransaction::getOrderId)
                .distinct()
                .collect(toSet());

        return orderBills.stream()
                .filter(OrderBill::isProductChargeSettled)
                .filter(orderBill -> !haveCreatedTransactionOrderIds.contains(orderBill.getOrderId()))
                .collect(toList());
    }

    public void validate(List<Map<String, Object>> bodyList, List<OrderBill> orderBillsFromDb, UploadExcelResponse uploadExcelResponse) {
        List<String> orderIds = bodyList.stream()
                .map(item -> item.get("orderId") == null ? "" : item.get("orderId").toString())
                .collect(Collectors.toList());
        uploadExcelResponse.setStatusErrorOrderIds(importationValidator.validate(bodyList, orderBillsFromDb));
        uploadExcelResponse.setDuplicatedOrderIds(checkDuplicatedOrderIds(orderIds));
        uploadExcelResponse.setNonExistOrderIds(checkNonExistOrderIds(orderBillsFromDb, orderIds));
    }

    private List<String> checkNonExistOrderIds(List<OrderBill> orderBillsFromDb, List<String> orderIds) {
        Map<String, OrderBill> orderBillMap = orderBillsFromDb.stream().collect(Collectors.toMap(OrderBill::getOrderId, Function.identity(), (newValue, oldValue) -> newValue));
        return orderIds.stream().filter(id -> orderBillMap.get(id) == null).collect(Collectors.toList());
    }

    private List<String> checkDuplicatedOrderIds(List<String> orderIds) {
        Map<String, List<String>> result = orderIds.stream().collect(Collectors.groupingBy(Function.identity()));
        return result.entrySet().stream().filter(entry -> entry.getValue().size() > 1).map(Map.Entry::getKey).collect(toList());
    }
}
