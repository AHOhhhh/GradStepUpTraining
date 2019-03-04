package com.thoughtworks.mall_order.service;

import com.thoughtworks.mall_order.entity.Order;
import com.thoughtworks.mall_order.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    private OrderService orderService;

    @Before
    public void setUp() throws Exception {
        orderService = new OrderService(orderRepository);
    }

    @Test
    public void getAll() {
        //given
        long userId = 1;
        List<Order> orders = new ArrayList<>();
        //when
        given(orderRepository.findByUserId(anyLong())).willReturn(orders);
        //then
        assertThat(orderService.getAll(userId).size()).isEqualTo(0);
    }

    @Test
    public void get() {
        //given
        long orderId = 1;
        Order order = new Order();
        //when
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
        //then
        assertThat(orderService.get(orderId).getId()).isEqualTo(0);

    }
}