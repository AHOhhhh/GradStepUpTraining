package fun.hercules.order.order.business.acg.service;

import fun.hercules.order.order.business.acg.client.AcgBusinessClient;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.acg.domain.Goods;
import fun.hercules.order.order.business.acg.domain.LengthUnit;
import fun.hercules.order.order.business.acg.domain.PriceRequest;
import fun.hercules.order.order.business.acg.domain.PriceResponse;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.dto.order.AcgCreateOrderRequest;
import fun.hercules.order.order.business.acg.dto.order.AcgCreateOrderResponse;
import fun.hercules.order.order.business.acg.dto.price.AcgGetPriceRequest;
import fun.hercules.order.order.business.acg.dto.price.AcgGetPriceResponse;
import fun.hercules.order.order.business.acg.validation.AcgOrderValidator;
import fun.hercules.order.order.platform.order.dto.OfflinePaymentInfo;
import fun.hercules.order.order.platform.order.dto.payment.AcgUpdateOrderToPaidRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
/*
 * This service is based on interface documentation for YZRA
 * When YZRA implements the interface defined on the doc, this service will be used
 */
public class AcgIntegrationService {

    private final AcgBusinessClient businessClient;

    private AcgOrderValidator validator;

    private static final Integer CUBIC_CENTIMETER_TO_CUBIC_METER = 1000000;


    public AcgIntegrationService(AcgBusinessClient businessClient, AcgOrderValidator validator) {
        this.businessClient = businessClient;
        this.validator = validator;
    }

    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformAdmin', 'PlatformService')")
    public PriceResponse getOrderPrice(PriceRequest request) {
        validator.validatePriceRequest(request);

        AcgGetPriceResponse cost = businessClient.getPrice(new AcgGetPriceRequest(request));

        BigDecimal pickUpPrice = request.getPickUpAddress().getId() != null ? cost.getPickUpPrice() : BigDecimal.ZERO;
        BigDecimal dropOffPrice = request.getDropOffAddress().getId() != null ? cost.getDropOffPrice() : BigDecimal.ZERO;
        BigDecimal airlinePrice = getAirlinePrice(request.getGoods(), cost);
        BigDecimal total = pickUpPrice.add(dropOffPrice).add(airlinePrice);

        return new PriceResponse(pickUpPrice, dropOffPrice, airlinePrice, total);
    }

    private BigDecimal getAirlinePrice(List<Goods> goodsList, AcgGetPriceResponse cost) {
        BigDecimal sizePrice = BigDecimal.ZERO;
        BigDecimal weightPrice = BigDecimal.ZERO;

        for (Goods goods : goodsList) {
            BigDecimal goodCount = new BigDecimal(goods.getQuantity());
            BigDecimal volume = BigDecimal.valueOf(goods.getSize().getHeight()
                    * goods.getSize().getLength()
                    * goods.getSize().getWidth()
                    / CUBIC_CENTIMETER_TO_CUBIC_METER);
            sizePrice = sizePrice.add(cost.getUnitPriceByVolume().multiply(goodCount).multiply(volume));
            weightPrice = weightPrice.add(cost.getUnitPriceByWeight().multiply(goodCount).multiply(goods.getWeight().getValue()));
        }

        return sizePrice.compareTo(weightPrice) == 1 ? sizePrice : weightPrice;
    }

    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformAdmin', 'PlatformService')")
    public AcgCreateOrderResponse createOrder(AcgOrder acgOrder) {
        AcgCreateOrderRequest acgCreateOrderRequest = new AcgCreateOrderRequest();
        acgCreateOrderRequest.setOrderId(acgOrder.getId());
        acgCreateOrderRequest.setFromAirportCode(acgOrder.getShippingInfo().getDeparture().getAirportId());
        acgCreateOrderRequest.setToAirportCode(acgOrder.getShippingInfo().getArrival().getAirportId());
        acgCreateOrderRequest.setGoodName((acgOrder.getGoods().get(0).getName()));
        acgCreateOrderRequest.setGoodWeight(acgOrder.getGoods().get(0).getWeight().getValue());
        acgCreateOrderRequest.setGoodVolume(acgOrder.getGoods().get(0).getVolume(LengthUnit.CM));
        acgCreateOrderRequest.setExpectDeliverTime(acgOrder.getShippingInfo().getEstimatedDeliveryTime());
        return businessClient.createOrder(acgCreateOrderRequest);
    }

    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformAdmin', 'PlatformService')")
    public AcgResponse updateOrderToPaid(OfflinePaymentInfo offlinePaymentInfo) {
        String orderId = offlinePaymentInfo.getOrderId();
        AcgUpdateOrderToPaidRequest request = new AcgUpdateOrderToPaidRequest(new AcgUpdateOrderToPaidRequest.Transactions());
        return businessClient.updateOrderToPaid(orderId, request);
    }


}
