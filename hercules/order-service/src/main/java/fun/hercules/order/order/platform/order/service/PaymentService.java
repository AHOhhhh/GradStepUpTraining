package fun.hercules.order.order.platform.order.service;

import com.google.common.collect.ImmutableList;
import fun.hercules.order.order.clients.CachedUserClient;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.common.exceptions.ConflictException;
import fun.hercules.order.order.common.exceptions.NotFoundException;
import fun.hercules.order.order.platform.exports.BusinessOrder;
import fun.hercules.order.order.platform.order.client.PaymentClient;
import fun.hercules.order.order.platform.order.dto.DeferredOrderInfo;
import fun.hercules.order.order.platform.order.dto.DeferredTransaction;
import fun.hercules.order.order.platform.order.dto.OfflinePaymentInfo;
import fun.hercules.order.order.platform.order.dto.PaymentInfo;
import fun.hercules.order.order.platform.order.dto.PaymentNotification;
import fun.hercules.order.order.platform.order.dto.RefundInfo;
import fun.hercules.order.order.platform.order.dto.TransactionNotification;
import fun.hercules.order.order.platform.order.model.OrderBill;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.PayChannel;
import fun.hercules.order.order.platform.order.model.PayMethod;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.model.PaymentTransaction;
import fun.hercules.order.order.platform.order.model.Refund;
import fun.hercules.order.order.platform.order.model.TransactionType;
import fun.hercules.order.order.platform.order.repository.PaymentRepository;
import fun.hercules.order.order.platform.order.repository.PaymentTransactionRepository;
import fun.hercules.order.order.platform.order.validators.ValidatorForUserAccess;
import fun.hercules.order.order.platform.user.CurrentUser;
import fun.hercules.order.order.platform.user.Role;
import fun.hercules.order.order.utils.PaymentUtils;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.lang.Strings;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fun.hercules.order.order.common.errors.ErrorCode.PAYMENT_NOT_FOUND;

@Slf4j
@Service
public class PaymentService {

    public static final int MAX_LENGTH = 50;
    private final PaymentRepository paymentRepository;

    private final PaymentRequestService paymentRequestService;

    private final PaymentTransactionRepository paymentTransactionRepository;

    private final CurrentUser currentUser;

    private final OrderService orderService;

    private final PaymentClient paymentClient;

    private final OrderBillService orderBillService;

    private final CachedUserClient cachedUserClient;

    private ValidatorForUserAccess validator;

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentRequestService paymentRequestService,
                          PaymentTransactionRepository paymentTransactionRepository,
                          CurrentUser currentUser,
                          OrderService orderService,
                          PaymentClient paymentClient,
                          OrderBillService orderBillService,
                          CachedUserClient cachedUserClient,
                          ValidatorForUserAccess validator) {
        this.paymentRepository = paymentRepository;
        this.paymentRequestService = paymentRequestService;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.currentUser = currentUser;
        this.orderService = orderService;
        this.paymentClient = paymentClient;
        this.orderBillService = orderBillService;
        this.cachedUserClient = cachedUserClient;
        this.validator = validator;
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('PlatformService')")
    public void createRefundTransaction(String paymentId, TransactionNotification notification) {
        if (notification.getType() == TransactionType.Income) {
            Payment payment = paymentRepository.getOne(paymentId);

            List<BusinessOrder> paymentOrders = payment.getPaymentRequests()
                    .stream()
                    .distinct()
                    .map(o -> orderService.get(o.getOrderType().toString(), o.getOrderId()))
                    .collect(Collectors.toList());

            List<PaymentTransaction> existPaymentTransactions =
                    paymentTransactionRepository.findByTransactionId(notification.getTransactionId());

            paymentOrders.forEach(order -> {
                boolean isExisted = existPaymentTransactions.stream().anyMatch(t -> t.getOrderId().equals(order.getId()));
                if (!isExisted) {
                    createPaymentTransaction(notification, order);
                    orderService.handleOrderAfterRefund(order, notification.getPayCustId());
                }
                orderService.createRefundNotification(order, notification);
            });
        }
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('PlatformService')")
    public void updatePaymentStatus(String paymentId, PaymentNotification paymentNotification) {
        Payment payment = paymentRepository.getOne(paymentId);

        final PaymentStatus newStatus = paymentNotification.getStatus();
        final PaymentStatus oldStatus = payment.getStatus();

        payment.setStatus(newStatus);
        payment.setPayRepeated(paymentNotification.isPayRepeated());
        payment.setPaidTime(paymentNotification.getPaidTime());
        payment.setPayUserId(paymentNotification.getPaymentUserId());
        payment.getPaymentRequests().forEach(paymentRequest -> {
            paymentRequest.setPaymentStatus(newStatus);
            paymentRequest.setPaidTime(paymentNotification.getPaidTime());
        });

        payment = paymentRepository.save(payment);

        syncPaymentTransactions(payment);

        if (shouldUpdateOfflineOrderStatus(payment) || shouldUpdateOnLineOrderStatus(payment, oldStatus, newStatus)) {
            log.info("handle payment {} from {} to {}", paymentId, oldStatus, newStatus);
            orderService.handleOrderAfterPayment(payment, paymentNotification.getOperatorId());
        }
    }

    public void createPaymentTransactions(List<DeferredTransaction> deferredTransactions, List<BusinessOrder> orderList, Map<String, Payment> paymentMap) {
        Map<String, BusinessOrder> orderMap = orderList.stream().collect(Collectors.toMap(BusinessOrder::getId, Function.identity()));
        List<PaymentTransaction> paymentTransactions = deferredTransactions
                .stream()
                .map(deferredTransaction -> {
                    String paymentId = deferredTransaction.getPaymentId();
                    String orderId = paymentMap.get(paymentId).getPaymentRequests().get(0).getOrderId();
                    log.info("Get payment transaction by order id {} and order is {} ", orderId, orderMap.get(orderId));
                    return getPaymentTransaction(deferredTransaction, orderMap.get(orderId));
                })
                .collect(Collectors.toList());
        paymentTransactionRepository.saveAll(paymentTransactions);
    }

    private boolean shouldUpdateOnLineOrderStatus(Payment payment, PaymentStatus oldStatus, PaymentStatus newStatus) {
        return oldStatus != newStatus && !payment.getPayMethod().equalsIgnoreCase("OFFLINE")
                && newStatus.equals(PaymentStatus.Success);
    }

    private boolean shouldUpdateOfflineOrderStatus(Payment payment) {
        return payment.getPayMethod().equalsIgnoreCase("OFFLINE");
    }

    private void syncPaymentTransactions(Payment payment) {
        List<TransactionNotification> transactionNotifications = paymentClient.getPaymentTransactions(payment.getId());

        List<BusinessOrder> paymentOrders = payment.getPaymentRequests()
                .stream()
                .distinct()
                .map(o -> orderService.get(o.getOrderType().toString(), o.getOrderId()))
                .collect(Collectors.toList());

        transactionNotifications.forEach(notification -> {

            List<PaymentTransaction> existPaymentTransactions =
                    paymentTransactionRepository.findByTransactionId(notification.getTransactionId());

            if (existPaymentTransactions.size() > 0) {
                updatePaymentTransactionsStatus(notification.getPayStatus(), existPaymentTransactions);
            }

            paymentOrders.forEach(order -> {
                boolean isExisted = existPaymentTransactions.stream().anyMatch(t -> t.getOrderId().equals(order.getId()));
                if (!isExisted) {
                    createPaymentTransaction(notification, order);
                }
            });
        });
    }

    private void updatePaymentTransactionsStatus(PaymentStatus newStatus, List<PaymentTransaction> oldTransactions) {
        oldTransactions.stream()
                .filter(t -> t.getStatus() != newStatus)
                .forEach(t -> t.setStatus(newStatus));

        paymentTransactionRepository.saveAll(oldTransactions);
    }

    private void createPaymentTransaction(TransactionNotification notification, BusinessOrder order) {
        PaymentTransaction paymentTransaction = getPaymentTransaction(notification, order);

        paymentTransactionRepository.save(paymentTransaction);
    }

    private PaymentTransaction getPaymentTransaction(TransactionNotification notification, BusinessOrder order) {
        return PaymentTransaction.builder()
                    .transactionId(notification.getTransactionId())
                    .paymentId(notification.getPaymentId())
                    .payMethod(notification.getPayMethod())
                    .status(notification.getPayStatus())
                    .transactionType(notification.getType())
                    .amount(notification.getPayAmount())
                    .paidTime(notification.getPaidTime())
                    .currency(notification.getCurrency())
                    .comment(notification.getComment())
                    .orderId(order.getId())
                    .enterpriseId(order.getEnterpriseId())
                    .vendor(order.getVendor())
                    .orderType(OrderType.of(order.getOrderType()))
                    .build();
    }

    public void validateOnlineOrder(PaymentInfo paymentInfo) {

        if (paymentInfo.getPayAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(ErrorCode.INVALID_PAYMENT_PRICE, "payment amount should be more than zero.");
        }

        if (!String.valueOf(paymentInfo.getPayAmount()).matches("^[0-9]{0,16}(\\.[0-9]{0,2})?$")) {
            throw new BadRequestException(ErrorCode.INVALID_PAYMENT_PRICE,
                    "payment amount format wrong, should contains up to 2 bit decimal and 16 bit integer");
        }

        if (!paymentInfo.getBusinessType().toLowerCase().matches("^(wms|acg)$")) {
            throw new BadRequestException(ErrorCode.ORDER_TYPE_NOT_FOUND, String.format("No such order type: %s", paymentInfo.getBusinessType()));
        }

        List<PaymentRequest> paymentRequests = this.paymentRequestService.findAllByPaymentRequestIds(paymentInfo.getPayRequestIds());
        List<String> foundIds = paymentRequests.stream().map(PaymentRequest::getId).collect(Collectors.toList());

        if (!CollectionUtils.isEqualCollection(foundIds, paymentInfo.getPayRequestIds())) {
            List<String> payRequestIds = paymentInfo.getPayRequestIds();
            payRequestIds.removeAll(foundIds);
            throw new NotFoundException(ErrorCode.PAYMENT_REQUEST_NOT_FOUND, String.format("Payment request not exist, ids=%s", payRequestIds));
        }

        double totalPrice = paymentRequests.stream().mapToDouble(req -> req.getAmount().doubleValue()).sum();
        if (paymentInfo.getPayAmount().doubleValue() != totalPrice) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST,
                    String.format("payment request amount wrong, requestIds:%s",
                            Strings.collectionToCommaDelimitedString(paymentInfo.getPayRequestIds())));
        }
        if (!currentUser.getUserId().equals(paymentInfo.getPayCustId())) {
            throw new BadRequestException(ErrorCode.INVALID_USER_INFO, "pay_cust_user_id is not current user");
        }
    }


    public void validateOfflineOrder(OfflinePaymentInfo offlinePaymentInfo) {
        this.validateOnlineOrder(offlinePaymentInfo);

        if (offlinePaymentInfo.getDepositBank() == null
                || offlinePaymentInfo.getCollectionAccountName() == null
                || offlinePaymentInfo.getCollectionAccountNumber() == null) {
            throw new BadRequestException(ErrorCode.MISSING_ACCOUNT_INFO, "missing account information");
        }

        if (offlinePaymentInfo.getBankTransactionNumber() == null) {
            throw new BadRequestException(ErrorCode.MISSING_BANK_TRANSACTION_NUMBER, "missing bank transaction number");
        }
    }

    public Payment createPayment(PaymentInfo paymentInfo) {

        List<Payment> existedPayments = this.paymentRepository.findDistinctByPaymentRequestsIdInAndDeletedIsFalse(paymentInfo.getPayRequestIds());

        Payment payment;

        Currency currency = Currency.getInstance(paymentInfo.getCurrencyCode());
        if (CollectionUtils.isEmpty(existedPayments)) {
            payment = Payment.builder()
                    .amount(paymentInfo.getPayAmount())
                    .status(PaymentStatus.PayInProcess)
                    .currency(currency)
                    .payChannel(paymentInfo.getPayChannel().name())
                    .payMethod(paymentInfo.getPayMethod().name())
                    .build();
        } else {
            checkExistedPayments(paymentInfo, existedPayments);
            payment = existedPayments.get(0);
            payment.setPayMethod(paymentInfo.getPayMethod().name());
            payment.setPayChannel(paymentInfo.getPayChannel().name());
        }

        try {
            orderBillService.saveOrUpdateOrderBill(paymentInfo);
            OrderBill orderBill = orderBillService.findOrderBillByOrderId(paymentInfo.getOrderId());
            orderBill.setPayMethod(paymentInfo.getPayMethod());
            orderBill.setPayChannel(paymentInfo.getPayChannel());
            orderBill.setCurrency(currency);
            orderBill.initDefaultStatus();
            orderBillService.save(orderBill);
        } catch (NotFoundException e) {
            log.warn("order %s has no order bill", paymentInfo.getOrderId());
        }

        List<PaymentRequest> paymentRequests = paymentRequestService.findAllByPaymentRequestIds(paymentInfo.getPayRequestIds());
        payment.setPaymentRequests(paymentRequests);

        return this.paymentRepository.save(payment);
    }

    private void checkExistedPayments(PaymentInfo paymentInfo, List<Payment> existedPayments) {
        if (existedPayments.size() > 1) {
            throw new ConflictException(ErrorCode.PAYMENT_CONFLICT,
                    String.format("more than one payment existed for payment request(%s)", paymentInfo.getPayRequestIds()));
        }
        Payment existedPayment = existedPayments.get(0);
        List<String> existedRequestIds = existedPayment.getPaymentRequests().stream().map(PaymentRequest::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEqualCollection(existedRequestIds, paymentInfo.getPayRequestIds())) {
            throw new ConflictException(ErrorCode.PAYMENT_CONFLICT,
                    String.format("payment existed with different requestId: %s", existedRequestIds));
        }
    }

    public List<PaymentRequest> getPaymentRequestsByOrderId(String orderId) {
        List<PaymentRequest> paymentRequests = this.paymentRequestService.findAllPaymentRequestsByOrderId(orderId);
        if (Collections.isEmpty(paymentRequests)) {
            throw new NotFoundException(ErrorCode.PAYMENT_REQUEST_NOT_FOUND,
                    String.format("order(id=%s) has no payment request", orderId));
        }
        return paymentRequests;
    }

    public List<Payment> findPayments(List<PaymentRequest> paymentRequests) {
        return paymentRepository.findDistinctByPaymentRequestsInAndDeletedIsFalseOrderByCreatedAtDesc(paymentRequests);
    }

    public List<Payment> findByOrderId(String orderId) {
        List<PaymentRequest> paymentRequests = this.paymentRequestService.findAllPaymentRequestsByOrderId(orderId);

        return findPayments(paymentRequests);
    }

    public List<Payment> findByOrderIds(List<String> orderIds) {
        List<PaymentRequest> paymentRequests = this.paymentRequestService.findAllPaymentRequestsByOrderIds(orderIds);
        return findPayments(paymentRequests);
    }

    @PreAuthorize("hasAnyAuthority('PlatformAdmin')")
    public void refund(String orderId, Refund refund) {
        validate(orderId, refund);

        List<Payment> payments = findByOrderId(orderId);
        if (CollectionUtils.isEmpty(payments)) {
            throw new NotFoundException(PAYMENT_NOT_FOUND, "can not find payment by orderId " + orderId);
        }

        Payment payment = payments.get(0);
        RefundInfo refundInfo = new RefundInfo(payment.getId(),
                refund.getRefundAmount(),
                currentUser.getUserId(),
                cachedUserClient.getUserNameById(currentUser.getUserId()),
                payment.getCurrency().toString(),
                refund.getComments());
        paymentClient.createRefundTransaction(refundInfo);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    private void validate(String orderId, Refund refund) {
        BusinessOrder order = orderService.getByOrderId(orderId);
        ensureAccessOfCurrentUser(order);
        ensureOrderCanBeRefunded(order);

        validateField(refund.getRefundAmount(), "refundAmount");
        validateField(refund.getRefundMethod(), "refundMethod");
        validateField(refund.getComments(), "comments");

        if (refund.getRefundAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "refundAmount should greater than 0");
        }

        if (refund.getComments().length() > MAX_LENGTH) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "comment length should less than 50");
        }
    }

    private void ensureOrderCanBeRefunded(BusinessOrder order) {
        ImmutableList<OrderStatus> statuses = ImmutableList.of(
                OrderStatus.of(OrderStatus.Type.Paid),
                OrderStatus.of(OrderStatus.Type.OrderTracking),
                OrderStatus.of(OrderStatus.Type.Closed)
        );

        if (!(statuses.contains(order.getStatus()) || isOrderCancelledAndOrderHasRefunded(order))) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, "order should not be refunded");
        }
    }

    private Boolean isOrderCancelledAndOrderHasRefunded(BusinessOrder order) {
        return order.getStatus().equals(OrderStatus.of(OrderStatus.Type.Cancelled)) && order.getRefundStatus().equals(Boolean.TRUE);
    }

    private void validateField(Object field, String fieldName) {
        if (StringUtils.isEmpty(field)) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST, String.format("%s should not be null or empty", fieldName));
        }
    }

    private void ensureAccessOfCurrentUser(BusinessOrder order) {
        if (currentUser.getRole().equals(Role.PlatformAdmin.name())) {
            validator.validateAccess(order.getOrderType(), currentUser);
        }
    }

    public String getPaymentRequestIdsByOrderId(String orderId) {
        return PaymentUtils.joinPaymentRequestIds(getPaymentRequestsByOrderId(orderId));
    }

    public void setSuccessStatus(Payment payment) {
        payment.setPaidTime(Instant.now());
        payment.setStatus(PaymentStatus.Success);
        save(payment);
    }

    public Payment createDefermentPayment(BusinessOrder order) {
        List<String> requests = paymentRequestService.findAllPaymentRequestsByOrderId(order.getId())
                .stream().map(request -> request.getId()).collect(Collectors.toList());

        PaymentInfo paymentInfo = new PaymentInfo(order.getId(),
                requests,
                order.getTotalPrice(),
                order.getTotalPrice(),
                order.getCurrency().getCurrencyCode(),
                order.getOrderType(),
                currentUser.getUserId(),
                cachedUserClient.getUserNameById(currentUser.getUserId()),
                "",
                PayChannel.WMS,
                PayMethod.DEFERMENT);
        return createPayment(paymentInfo);
    }


    public void createDeferredPayTrans(BusinessOrder order, Payment payment) {
        DeferredOrderInfo deferredOrderInfo = DeferredOrderInfo.builder()
                .paymentId(payment.getId())
                .orderAmount(order.getTotalPrice())
                .payAmount(payment.getAmount())
                .currencyCode(order.getCurrency().getCurrencyCode())
                .payCustId(order.getUserId())
                .payCustName(cachedUserClient.getUserNameById(order.getUserId()))
                .build();

        paymentClient.createDeferredPayTrans(deferredOrderInfo);
    }

    @Async
    @SneakyThrows
    public void autoConfirmPayment(String transactionId) {
        Thread.sleep(3000);
        log.info("confirming transition {}", transactionId);
        paymentClient.updateOfflineTransactionStatus(fun.hercules.order.order.platform.order.dto.OfflineNotifyRequest.builder()
                .transactionId(transactionId)
                .isConfirmed(true)
                .comment("auto confirmed")
                .build());
    }
}
