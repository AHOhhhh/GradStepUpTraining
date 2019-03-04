package fun.hercules.order.order.platform.order.service;

import fun.hercules.order.order.JUnitWebAppTest;
import fun.hercules.order.order.OrderIntegrationTestBase;
import fun.hercules.order.order.common.constants.ConfigProperties;
import fun.hercules.order.order.platform.order.model.OrderType;
import fun.hercules.order.order.platform.order.model.PayMethod;
import fun.hercules.order.order.platform.order.model.Payment;
import fun.hercules.order.order.platform.order.model.PaymentRequest;
import fun.hercules.order.order.platform.order.model.PaymentStatus;
import fun.hercules.order.order.platform.order.model.PaymentTransaction;
import fun.hercules.order.order.platform.order.model.TransactionType;
import fun.hercules.order.order.platform.order.repository.PaymentRepository;
import fun.hercules.order.order.platform.order.repository.PaymentTransactionRepository;
import fun.hercules.order.order.platform.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@JUnitWebAppTest
public class PaymentTransactionServiceTest extends OrderIntegrationTestBase {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    private final ZoneId defaultZone = ConfigProperties.getInstance().getTimeZone();

    private final PageRequest defaultPageRequest =
            new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "updatedAt"));

    @Test
    public void findShouldReturnCorrectlyWhenQueryWithEmptyArgs() {

        String orderIdA = "111712051745275331838541";
        String orderIdB = "111712051745275331838542";

        Payment payment = createPayment(PaymentStatus.Success, Lists.newArrayList());

        createPaymentTransaction(orderIdA, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());

        switchRole(Role.PlatformAdmin);

        Page<PaymentTransaction> transactions = paymentTransactionService
                .find(null, null, null, null, null, defaultPageRequest);

        assertThat(transactions).hasSize(2);
    }

    @Test
    public void findShouldReturnCorrectlyAndSortDescByUpdatedAt() throws InterruptedException {

        String orderIdA = "111712051745275331838541";
        String orderIdB = "111712051745275331838542";

        Payment payment = createPayment(PaymentStatus.Success, Lists.newArrayList());

        final PaymentTransaction transactionA =
                createPaymentTransaction(orderIdA, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        TimeUnit.MILLISECONDS.sleep(10);
        final PaymentTransaction transactionB =
                createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        TimeUnit.MILLISECONDS.sleep(10);
        final PaymentTransaction transactionC =
                createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());

        switchRole(Role.PlatformAdmin);

        Page<PaymentTransaction> transactions = paymentTransactionService
                .find(null, null, null, null, null, defaultPageRequest);

        assertThat(transactions.getContent().get(0)).isEqualTo(transactionC);
        assertThat(transactions.getContent().get(1)).isEqualTo(transactionB);
        assertThat(transactions.getContent().get(2)).isEqualTo(transactionA);
    }

    @Test
    public void findShouldReturnCorrectlyAndPaging() throws InterruptedException {

        String orderIdA = "111712051745275331838541";
        String orderIdB = "111712051745275331838542";

        Payment payment = createPayment(PaymentStatus.Success, Lists.newArrayList());

        createPaymentTransaction(orderIdA, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        TimeUnit.MILLISECONDS.sleep(10);
        final PaymentTransaction transactionB =
                createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        TimeUnit.MILLISECONDS.sleep(10);
        final PaymentTransaction transactionC =
                createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());

        PageRequest pageRequest = new PageRequest(0, 2, new Sort(Sort.Direction.DESC, "updatedAt"));

        switchRole(Role.PlatformAdmin);

        Page<PaymentTransaction> transactions = paymentTransactionService
                .find(null, null, null, null, null, pageRequest);

        assertThat(transactions.getTotalElements()).isEqualTo(3);
        assertThat(transactions.getTotalPages()).isEqualTo(2);
        assertThat(transactions.getContent()).hasSize(2);
        assertThat(transactions.getContent().get(0)).isEqualTo(transactionC);
        assertThat(transactions.getContent().get(1)).isEqualTo(transactionB);
    }

    @Test
    public void findShouldReturnCorrectlyWhenQueryOnlyByOrderId() {

        String orderIdA = "111712051745275331838541";
        String orderIdB = "111712051745275331838542";

        Payment payment = createPayment(PaymentStatus.Success, Lists.newArrayList());

        createPaymentTransaction(orderIdA, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());

        switchRole(Role.PlatformAdmin);

        Page<PaymentTransaction> transactions = paymentTransactionService
                .find(orderIdA, Lists.newArrayList(), null, null, null, defaultPageRequest);

        assertThat(transactions)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("orderId", orderIdA);
    }

    @Test
    public void findShouldReturnCorrectlyWhenQueryOnlyByEnterpriseIds() {

        String orderIdA = "111712051745275331838541";
        String orderIdB = "111712051745275331838542";

        Payment payment = createPayment(PaymentStatus.Success, Lists.newArrayList());

        createPaymentTransaction(orderIdA, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        PaymentTransaction transactionB = createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        PaymentTransaction transactionC = createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());

        switchRole(Role.PlatformAdmin);

        Page<PaymentTransaction> transactions = paymentTransactionService
                .find(null, Lists.newArrayList(
                        transactionB.getEnterpriseId(), transactionC.getEnterpriseId()
                ), null, null, null, defaultPageRequest);

        assertThat(transactions).hasSize(2);
    }

    @Test
    public void findShouldReturnCorrectlyWhenQueryOnlyByStartDateAndEndDate() {

        String orderIdA = "111712051745275331838541";
        String orderIdB = "111712051745275331838542";

        Payment payment = createPayment(PaymentStatus.Success, Lists.newArrayList());

        createPaymentTransaction(orderIdA, UUID.randomUUID().toString(), payment.getId(),
                LocalDate.of(2017, 12, 1));
        createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(),
                LocalDate.of(2017, 12, 2));
        createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(),
                LocalDate.of(2017, 12, 3));

        switchRole(Role.PlatformAdmin);

        Page<PaymentTransaction> transactions = paymentTransactionService
                .find(null, null,
                        LocalDate.of(2017, 12, 1),
                        LocalDate.of(2017, 12, 2), null, defaultPageRequest);

        assertThat(transactions).hasSize(2);
    }

    @Test
    public void findShouldReturnCorrectlyWhenQueryOnlyByPaymentStatus() {

        String orderIdA = "111712051745275331838541";
        String orderIdB = "111712051745275331838542";

        Payment payment = createPayment(PaymentStatus.Success, Lists.newArrayList());

        PaymentTransaction transactionA =
                createPaymentTransaction(orderIdA, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());
        createPaymentTransaction(orderIdB, UUID.randomUUID().toString(), payment.getId(), LocalDate.now());

        transactionA.setStatus(PaymentStatus.Fail);

        switchRole(Role.PlatformAdmin);

        Page<PaymentTransaction> transactions = paymentTransactionService
                .find(null, null, null, null, Lists.newArrayList(PaymentStatus.Fail), defaultPageRequest);

        assertThat(transactions)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("status", PaymentStatus.Fail);
    }

    private PaymentTransaction createPaymentTransaction(String orderId,
                                                        String enterpriseId,
                                                        String paymentId,
                                                        LocalDate paidTime) {
        PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .paymentId(paymentId)
                .payMethod(PayMethod.ONLINE)
                .status(PaymentStatus.Success)
                .transactionType(TransactionType.Income)
                .amount(BigDecimal.valueOf(99.99))
                .paidTime(getInstantByDate(paidTime))
                .currency(Currency.getInstance("CNY"))
                .comment("Comment")
                .orderId(orderId)
                .enterpriseId(enterpriseId)
                .vendor("acg")
                .orderType(OrderType.of(OrderType.Type.ACG))
                .build();

        paymentTransactionRepository.save(paymentTransaction);

        return paymentTransaction;
    }

    private Payment createPayment(PaymentStatus status,
                                  List<PaymentRequest> paymentRequests) {
        Payment payment = Payment.builder()
                .status(status)
                .payRepeated(false)
                .payChannel("PayChannel")
                .payMethod("ONLINE")
                .currency(Currency.getInstance("CNY"))
                .paymentRequests(paymentRequests)
                .build();

        paymentRepository.save(payment);

        return payment;
    }

    private Instant getInstantByDate(LocalDate date) {
        return date.atStartOfDay().atZone(defaultZone).toInstant();
    }
}