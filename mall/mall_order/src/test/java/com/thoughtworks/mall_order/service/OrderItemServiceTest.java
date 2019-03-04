package com.thoughtworks.mall_order.service;

import com.thoughtworks.mall_order.controller.request.AddOrderItemRequest;
import com.thoughtworks.mall_order.controller.request.CreateOrderItemRequest;
import com.thoughtworks.mall_order.entity.OrderItem;
import com.thoughtworks.mall_order.exception.OrderItemNotFoundException;
import com.thoughtworks.mall_order.repository.OrderItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    private OrderItemService orderItemService;

    @Before
    public void setUp() throws Exception {
        orderItemService = new OrderItemService(orderItemRepository);
    }

    @Test
    public void addAll() {
        //given
        CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest();
        CreateOrderItemRequest createOrderItemRequest2 = new CreateOrderItemRequest();
        List<CreateOrderItemRequest> createOrderItemRequests = new ArrayList<>();
        createOrderItemRequests.add(createOrderItemRequest);
        createOrderItemRequests.add(createOrderItemRequest2);
        long orderId = 1;
        //when
        orderItemService.addAll(orderId, createOrderItemRequests);
        //then
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
    }

    @Test
    public void add() {
        //given
        AddOrderItemRequest addOrderItemRequest = new AddOrderItemRequest();
        long orderId = 1;
        Optional<OrderItem> oldOrderItemOptional = Optional.of(new OrderItem());

        //when
        given(orderItemRepository.findByOrderIdAndProductId(anyLong(), anyLong())).willReturn(oldOrderItemOptional);
        given(orderItemRepository.save(any(OrderItem.class))).willReturn(new OrderItem());
        //then
        assertThat(orderItemService.add(orderId, addOrderItemRequest)).isEqualTo(0);
    }

    @Test
    public void update() {
        //given
        long orderItemId = 1;
        int count = 10;
        OrderItem orderItem = new OrderItem();
        //when
        given(orderItemRepository.findById(any())).willReturn(Optional.of(orderItem));
        //then
        orderItemService.update(orderItemId, count);
        verify(orderItemRepository,times(1)).save(any());
    }


    @Test(expected = OrderItemNotFoundException.class)
    public void update_not_found() {
        //given
        long orderItemId = 1;
        int count = 10;
        OrderItem orderItem = new OrderItem();
        //when
        given(orderItemRepository.findById(any())).willThrow(new OrderItemNotFoundException());
        //then
        orderItemService.update(orderItemId, count);
    }

    @Test
    public void remove() {
        //given
        long orderItemId = 1;
        OrderItem orderItem = new OrderItem();
        //when
        given(orderItemRepository.findById(any())).willReturn(Optional.of(orderItem));
        //then
        orderItemService.remove(orderItemId);
        verify(orderItemRepository,times(1)).delete(any());
    }
}