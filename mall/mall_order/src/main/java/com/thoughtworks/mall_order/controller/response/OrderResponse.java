package com.thoughtworks.mall_order.controller.response;

import com.thoughtworks.mall_order.entity.Order;

import java.sql.Timestamp;
import java.util.List;

public class OrderResponse {

    private long id;
    private Timestamp orderTime;
    private long userId;
    private List<OrderItemResponse> orderItems;


    public OrderResponse(Order order, List<OrderItemResponse> orderItemResponses) {
        this.id = order.getId();
        this.orderTime = order.getOrderTime();
        this.userId = order.getUserId();
        this.orderItems = orderItemResponses;
    }

    public OrderResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }
}
