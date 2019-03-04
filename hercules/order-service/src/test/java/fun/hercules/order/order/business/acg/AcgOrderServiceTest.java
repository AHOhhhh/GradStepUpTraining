package fun.hercules.order.order.business.acg;

import fun.hercules.order.order.business.acg.domain.AcgCompleteRequest;
import fun.hercules.order.order.business.acg.domain.AcgOrder;
import fun.hercules.order.order.business.acg.domain.LogisticsStatus;
import fun.hercules.order.order.business.acg.domain.UpdateLogisticStatusRequest;
import fun.hercules.order.order.business.acg.dto.AcgResponse;
import fun.hercules.order.order.business.acg.repository.AcgOrderRepository;
import fun.hercules.order.order.business.acg.service.AcgAirportService;
import fun.hercules.order.order.business.acg.service.AcgOrderService;
import fun.hercules.order.order.clients.service.OrderNotificationService;
import fun.hercules.order.order.platform.order.model.OrderLogistic;
import fun.hercules.order.order.platform.order.model.OrderStatus;
import fun.hercules.order.order.platform.order.repository.OrderLogisticRepository;
import fun.hercules.order.order.platform.order.service.PaymentRequestService;
import fun.hercules.order.order.platform.order.service.PaymentService;
import fun.hercules.order.order.platform.user.CurrentUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AcgOrderServiceTest {

    @Mock
    private AcgOrderRepository acgOrderRepository;
    @Mock
    private PaymentRequestService paymentRequestService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private AcgAirportService acgAirportService;
    @Mock
    private CurrentUser currentUser;
    @Mock
    private OrderNotificationService orderNotificationService;
    @Mock
    private UpdateLogisticStatusRequest updateLogisticStatusRequest;
    @Mock
    private OrderLogisticRepository orderLogisticRepository;

    private AcgOrderService acgOrderService;

    @Before
    public void setUp() {
        acgOrderService = new AcgOrderService(acgOrderRepository,
                paymentRequestService,
                paymentService,
                true,
                acgAirportService,
                currentUser,
                orderNotificationService,
                orderLogisticRepository);
    }

    @Test
    public void should_return_success() {
        //given
        AcgCompleteRequest acgCompleteRequest = new AcgCompleteRequest("order closed");
        AcgOrder acgOrder = new AcgOrder();
        acgOrder.setStatus(OrderStatus.of(OrderStatus.Type.OrderTracking));
        given(acgOrderRepository.findOneById(anyString())).willReturn(Optional.of(acgOrder));
        //when

        AcgResponse acgResponse = acgOrderService.onComplete(anyString(), acgCompleteRequest);
        //then
        assertThat(acgResponse.getStatus()).isEqualTo(AcgResponse.success().getStatus());
    }

    @Test
    public void should_return_fail_when_order_not_exist() {
        //given
        AcgCompleteRequest acgCompleteRequest = new AcgCompleteRequest("order closed");

        given(acgOrderRepository.findOneById(anyString())).willReturn(Optional.empty());
        //when

        AcgResponse acgResponse = acgOrderService.onComplete(anyString(), acgCompleteRequest);
        //then
        assertThat(acgResponse.getStatus()).isEqualTo(AcgResponse.failure().getStatus());
    }

    @Test
    public void should_return_fail_given_message_not_right() {
        //given
        AcgCompleteRequest acgCompleteRequest = new AcgCompleteRequest("");

        //when
        AcgResponse acgResponse = acgOrderService.onComplete("", acgCompleteRequest);

        //then
        assertThat(acgResponse.getStatus()).isEqualTo(AcgResponse.failure().getStatus());
    }

    @Test
    public void should_return_newest_logistic_status() {
        //given
        LogisticsStatus logisticsStatus = new LogisticsStatus("address1", Instant.now(), "张三");
        List<LogisticsStatus> logisticsStatusList = new ArrayList<>();
        logisticsStatusList.add(logisticsStatus);
        OrderLogistic orderLogistic = new OrderLogistic(logisticsStatus.getLogisticsStatusInfo(),
                logisticsStatus.getUpdateInfoTime(),
                logisticsStatus.getUpdateInfoUserName(),
                "1");
        when(updateLogisticStatusRequest.getLogisticsStatus()).thenReturn(logisticsStatusList);
        when(orderLogisticRepository.save(any(OrderLogistic.class))).thenReturn(orderLogistic);

        //when
        AcgResponse acgResponse = acgOrderService.updateLogisticsStatus("1", updateLogisticStatusRequest);

        //then
        assertThat(acgResponse.getStatus()).isEqualTo(AcgResponse.success().getStatus());
        verify(orderLogisticRepository,times(1)).save(any(OrderLogistic.class));
    }
}
