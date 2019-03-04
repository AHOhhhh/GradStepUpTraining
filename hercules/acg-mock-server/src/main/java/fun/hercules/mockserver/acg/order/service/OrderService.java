package fun.hercules.mockserver.acg.order.service;

import com.google.common.collect.ImmutableList;
import fun.hercules.mockserver.acg.order.clients.AcgBusinessClient;
import fun.hercules.mockserver.acg.order.exception.BadRequestException;
import fun.hercules.mockserver.acg.order.exception.NotFoundException;
import fun.hercules.mockserver.acg.order.model.AcgResponse;
import fun.hercules.mockserver.acg.order.model.Order;
import fun.hercules.mockserver.acg.order.model.booked.AcgBookedRequest;
import fun.hercules.mockserver.acg.order.model.cancel.AcgCancelRequest;
import fun.hercules.mockserver.acg.order.model.complete.AcgCompleteRequest;
import fun.hercules.mockserver.acg.order.model.create.AcgCreateOrderRequest;
import fun.hercules.mockserver.acg.order.model.create.AcgCreateOrderResponse;
import fun.hercules.mockserver.acg.order.model.logisticstatus.LogisticsStatus;
import fun.hercules.mockserver.acg.order.model.logisticstatus.UpdateLogisticStatusRequest;
import fun.hercules.mockserver.acg.order.model.payment.AcgUpdateOrderToPaidRequest;
import fun.hercules.mockserver.acg.order.model.plan.AcgPlanRequest;
import fun.hercules.mockserver.acg.order.model.price.AcgGetPriceRequest;
import fun.hercules.mockserver.acg.order.model.price.AcgGetPriceResponse;
import fun.hercules.mockserver.acg.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class OrderService {

    private static ImmutableList<String> DELEGATE_PREFIX = ImmutableList.copyOf("H171120151238".split(" "));

    private static ImmutableList<String> FLIGHT_NUMBERS = ImmutableList.copyOf("MU ZH HU MF CA CZ".split(" "));

    private static ImmutableList<String> PHONE_PREFIX = ImmutableList.copyOf("010 020 021 022 023 024 025 027 028 029".split(" "));

    private ConcurrentMap<String, Order> orderRepository;

    private AcgBusinessClient businessClient;

    public OrderService(AcgBusinessClient businessClient) {
        this.businessClient = businessClient;
        orderRepository = new ConcurrentHashMap<>();
    }

    public AcgGetPriceResponse getPrice(AcgGetPriceRequest request) {
        log.info("getPrice {}", JsonUtils.marshal(request));
        AcgGetPriceResponse response = new AcgGetPriceResponse();

        long fromSeed = request.getFromAirportId().hashCode();
        long toSeed = request.getToAirportId().hashCode();
        response.setUnitPriceByVolume(BigDecimal.valueOf(8000).multiply(getWeight(fromSeed + toSeed)).setScale(0, BigDecimal.ROUND_HALF_UP));
        response.setUnitPriceByWeight(BigDecimal.valueOf(10).multiply(getWeight(fromSeed + toSeed)).setScale(0, BigDecimal.ROUND_HALF_UP));
        response.setPickUpPrice(BigDecimal.valueOf(200).multiply(getWeight(fromSeed)).setScale(0, BigDecimal.ROUND_HALF_UP));
        response.setDropOffPrice(BigDecimal.valueOf(200).multiply(getWeight(toSeed)).setScale(0, BigDecimal.ROUND_HALF_UP));
        response.setStatus(AcgResponse.Status.SUCCESS);
        return response;

    }

    // from 0.5 ~ 1.5
    private BigDecimal getWeight(long seed) {
        return BigDecimal.valueOf(RandomUtils.nextDouble(new Random(seed)) + 0.5);
    }

    public AcgCreateOrderResponse createOrder(AcgCreateOrderRequest request) {
        log.info("[create←] request: {}", request);

        Order order = Order.builder()
                .id(request.getOrderId())
                .name(request.getGoodName())
                .delegateOrderId(String.valueOf(new Date().getTime()))
                .status(Order.Status.CREATED)
                .build();
        orderRepository.putIfAbsent(request.getOrderId(), order);

        AcgCreateOrderResponse response = new AcgCreateOrderResponse(order.getDelegateOrderId());

        log.info("[create←] response: {}", response);
        return response;
    }

    public AcgResponse updateOrderToPaid(String orderId, AcgUpdateOrderToPaidRequest request) {
        Order order = getOrder(orderId);
        log.info("[update payment status←] request: {}", request);
        AcgResponse acgResponse = AcgResponse.success();

        order.setStatus(Order.Status.PAYED);
        log.info("[update payment status←] response: {}", acgResponse);

        return acgResponse;
    }

    private void updateLogisticStatus(Order order) {
        Optional<LogisticsStatus> logisticsStatus = order.getLogisticsStatus();
        if (logisticsStatus.isPresent()) {
            try {
                UpdateLogisticStatusRequest request = new UpdateLogisticStatusRequest();
                request.setLogisticsStatus(Arrays.asList(logisticsStatus.get()));

                log.info("[update logistic status→] request: {}", request);
                AcgResponse response = businessClient.updateLogisticsStatus(order.getId(), request);
                log.info("[update logistic status→] response: {}", response);
            } catch (RuntimeException e) {
                log.warn("[update logistic status→] request failed", e);
            }
            if (order.updateTransportIndicator()) {
                order.setStatus(Order.Status.DELIVERED);
            }
        } else {
            order.setStatus(Order.Status.DELIVERED);
        }

    }

    private void updateBookedStatus(Order order) {
        AcgBookedRequest request = new AcgBookedRequest("flight booked successfully");
        log.info("[booked→] request: {}", request);
        try {
            AcgResponse response = businessClient.updateBookedStatus(order.getId(), request);
            log.info("[booked→] response: {}", response);
            order.setStatus(Order.Status.BOOKED);
        } catch (RuntimeException e) {
            log.warn("[booked→] request failed", e);
        }
    }

    private void complete(Order order) {
        AcgCompleteRequest request = new AcgCompleteRequest("order closed");
        log.info("[complete→] request: {}", request);
        try {
            AcgResponse response = businessClient.complete(order.getId(), request);
            log.info("[complete→] response: {}", response);
            order.setStatus(Order.Status.CLOSED);
        } catch (RuntimeException e) {
            log.warn("[complete→] request failed", e);
        }
    }

    private void updatePlan(Order order) {
        AcgPlanRequest request = new AcgPlanRequest(Instant.now().minus(2, ChronoUnit.DAYS),
                generatePhoneNumber(), generateFlightNo(),
                generateDelegateOrderId(),
                Instant.now().minus(1, ChronoUnit.DAYS),
                Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now().plus(2, ChronoUnit.DAYS),
                generatePhoneNumber());
        log.info("[plan→] request: {}", request);
        try {
            AcgResponse response = businessClient.updatePlan(order.getId(), request);
            order.setStatus(Order.Status.TRANSPORTING);
            log.info("[plan→] response: {}", response);
        } catch (RuntimeException e) {
            log.warn("[plan→] request failed", e);
        }
    }

    private String generateDelegateOrderId() {
        return String.format("%s%8d", DELEGATE_PREFIX.get(RandomUtils.nextInt(DELEGATE_PREFIX.size())),
                RandomUtils.nextInt(90000000) + 10000000);
    }

    private String generatePhoneNumber() {
        return String.format("%s-%8d", PHONE_PREFIX.get(RandomUtils.nextInt(PHONE_PREFIX.size())),
                RandomUtils.nextInt(90000000) + 10000000);
    }

    private String generateFlightNo() {
        return String.format("%s %04d", FLIGHT_NUMBERS.get(RandomUtils.nextInt(FLIGHT_NUMBERS.size())),
                RandomUtils.nextInt(10000));
    }

    public Collection<Order> listOrders() {
        return orderRepository.values();
    }

    public void trigger(String orderId) {
        Order order = getOrder(orderId);
        switch (order.getStatus()) {
            case CREATED:
                updateBookedStatus(order);
                break;
            case PAYED:
                updatePlan(order);
                break;
            case TRANSPORTING:
                updateLogisticStatus(order);
                break;
            case DELIVERED:
                complete(order);
                break;
            default:
                throw new BadRequestException(String.format("Invalid status %s for order %s", order.getStatus(), order.getId()));
        }
    }

    private Order getOrder(String orderId) {
        return Optional.ofNullable(orderRepository.get(orderId))
                .orElseThrow(() -> new NotFoundException(String.format("Order %s not found", orderId)));
    }

    @Scheduled(fixedDelay = 5000)
    void cleanCompletedOrders() {
//        log.info("try to clean up {} orders", orderRepository.size());
        orderRepository.values().stream()
                .filter(this::shouldClean)
                .forEach(order -> {
                    log.info("remove expired order {} @ {}", order.getId(), order.getTimestamp().atZone(ZoneId.of("Asia/Shanghai")).toString());
                    orderRepository.remove(order.getId());
                });


    }

    private boolean shouldClean(Order order) {
        // closed order will be cleaned after 60 minutes
        // other orders will be cleaned after 8 hours
        int timeout = order.getStatus() == Order.Status.CLOSED ? 60 : 8 * 60 * 60;
        return Instant.now().getEpochSecond() - order.getTimestamp().getEpochSecond() > timeout;

    }

    public AcgResponse cancelOrder(String orderId, AcgCancelRequest request) {
        Order order = getOrder(orderId);
        order.setStatus(Order.Status.CLOSED);
        return AcgResponse.success();
    }
}