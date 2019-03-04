package com.thoughtworks.mall_order.controller.request;

import java.util.List;

public class CreateOrderRequest {
    private List<CreateOrderItemRequest> createOrderItemRequestList;

    public List<CreateOrderItemRequest> getCreateOrderItemRequestList() {
        return createOrderItemRequestList;
    }

    public void setCreateOrderItemRequestList(List<CreateOrderItemRequest> createOrderItemRequestList) {
        this.createOrderItemRequestList = createOrderItemRequestList;
    }
}