package fun.hercules.order.order.business.acg;

import fun.hercules.order.order.OrderIntegrationTestBase;
import fun.hercules.order.order.business.acg.client.AcgBusinessClient;
import fun.hercules.order.order.business.acg.repository.AcgOrderRepository;
import fun.hercules.order.order.business.acg.service.AcgIntegrationService;
import fun.hercules.order.order.business.acg.service.AcgOrderService;
import fun.hercules.order.order.platform.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static fun.hercules.order.order.utils.ResourceUtils.loadFixture;

public class AcgOrderTestBase extends OrderIntegrationTestBase {
    protected final String orderContent = loadFixture("acg/create_acg_order.json");

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected OrderService orderService;

    @MockBean
    protected AcgBusinessClient acgBusinessClient;

    @MockBean
    AcgIntegrationService acgIntegrationService;

    @Autowired
    protected AcgOrderService acgOrderService;

    @Autowired
    protected AcgOrderRepository acgOrderRepository;
}
