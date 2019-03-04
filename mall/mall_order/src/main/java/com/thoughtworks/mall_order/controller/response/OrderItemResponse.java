package com.thoughtworks.mall_order.controller.response;

import com.thoughtworks.mall_order.entity.OrderItem;
import com.thoughtworks.mall_order.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class OrderItemResponse {
    private long id;
    private long orderId;
    private int productCount;
    private Product product;


    public OrderItemResponse(OrderItem orderItem, Product product) {
        this.id = orderItem.getId();
        this.orderId = orderItem.getOrderId();
        this.productCount = orderItem.getProductCount();
        this.product = product;
    }

    public OrderItemResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
