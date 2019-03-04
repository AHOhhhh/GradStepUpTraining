package com.thoughtworks.mall_order.service;

import com.thoughtworks.mall_order.controller.request.AddOrderItemRequest;
import com.thoughtworks.mall_order.controller.request.CreateOrderItemRequest;
import com.thoughtworks.mall_order.controller.response.OrderResponse;
import com.thoughtworks.mall_order.entity.Order;
import com.thoughtworks.mall_order.exception.OrderNotFoundException;
import com.thoughtworks.mall_order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAll(long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order get(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    }

    public long add(long userId, List<CreateOrderItemRequest> createOrderItemRequestList) {
        long orderId = createOrder(userId);
        orderItemService.addAll(orderId, createOrderItemRequestList);
        return orderId;
    }

    private long createOrder(long userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderTime(new Timestamp(System.currentTimeMillis()));
        return orderRepository.save(order).getId();
    }


    public long addItem(long orderId, AddOrderItemRequest addOrderItemRequest) {
        return orderItemService.add(orderId, addOrderItemRequest);
    }

    public void updateItem(long orderItemId, int count) {
        orderItemService.update(orderItemId,count);
    }

    public void removeItem(long orderItemId) {
        orderItemService.remove(orderItemId);

    }
}
