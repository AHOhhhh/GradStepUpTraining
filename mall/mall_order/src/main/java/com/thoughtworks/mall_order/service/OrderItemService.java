package com.thoughtworks.mall_order.service;


import com.thoughtworks.mall_order.controller.request.AddOrderItemRequest;
import com.thoughtworks.mall_order.controller.request.CreateOrderItemRequest;
import com.thoughtworks.mall_order.entity.OrderItem;
import com.thoughtworks.mall_order.exception.OrderItemNotFoundException;
import com.thoughtworks.mall_order.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class OrderItemService {


    private OrderItemRepository orderItemRepository;
    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    void addAll(long orderId, List<CreateOrderItemRequest> createOrderItemRequestList) {
        for (CreateOrderItemRequest orderItemRequest : createOrderItemRequestList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(orderItemRequest.getProductId());
            orderItem.setProductCount(orderItemRequest.getProductCount());
            orderItemRepository.save(orderItem);
        }
    }

    long add(long orderId, AddOrderItemRequest addOrderItemRequest) {
        Optional<OrderItem> oldOrderItem = orderItemRepository.findByOrderIdAndProductId(orderId, addOrderItemRequest.getProductId());
        if (oldOrderItem.isPresent()) {
            OrderItem orderItem = oldOrderItem.get();
            orderItem.setProductCount(orderItem.getProductCount() + addOrderItemRequest.getProductCount());
            return orderItemRepository.save(orderItem).getId();
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(addOrderItemRequest.getProductId());
            orderItem.setProductCount(addOrderItemRequest.getProductCount());
            return orderItemRepository.save(orderItem).getId();
        }
    }


    void update(long orderItemId, int count) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(OrderItemNotFoundException::new);
        orderItem.setProductCount(count);
        orderItemRepository.save(orderItem);

    }

    void remove(long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(OrderItemNotFoundException::new);
        orderItemRepository.delete(orderItem);
    }
}
