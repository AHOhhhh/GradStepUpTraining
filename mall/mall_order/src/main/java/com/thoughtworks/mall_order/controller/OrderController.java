package com.thoughtworks.mall_order.controller;

import com.thoughtworks.mall_order.controller.request.AddOrderItemRequest;
import com.thoughtworks.mall_order.controller.request.CreateOrderRequest;
import com.thoughtworks.mall_order.controller.response.OrderItemResponse;
import com.thoughtworks.mall_order.controller.response.OrderResponse;
import com.thoughtworks.mall_order.entity.Order;
import com.thoughtworks.mall_order.entity.OrderItem;
import com.thoughtworks.mall_order.exception.OrderItemNotFoundException;
import com.thoughtworks.mall_order.exception.OrderNotFoundException;
import com.thoughtworks.mall_order.model.Product;
import com.thoughtworks.mall_order.restService.ProductClient;
import com.thoughtworks.mall_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductClient productClient;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll(@RequestHeader long userId) {
        List<Order> orders = orderService.getAll(userId);

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            orderResponses.add(getOrderResponse(order));
        }
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> get(@PathVariable long orderId) {
        Order order = orderService.get(orderId);
        return ResponseEntity.ok(getOrderResponse(order));
    }


    @PostMapping
    public ResponseEntity add(@RequestHeader long userId, @RequestBody CreateOrderRequest createOrderRequest) {
        long orderId = orderService.add(userId, createOrderRequest.getCreateOrderItemRequestList());
        return ResponseEntity.created(URI.create(String.format("/orders/%s", orderId))).build();
    }


    @PostMapping("/{orderId}/orderitems")
    public ResponseEntity addItem(@PathVariable long orderId, @RequestBody AddOrderItemRequest addOrderItemRequest) {
        long orderItemId = orderService.addItem(orderId, addOrderItemRequest);
        return ResponseEntity.created(URI.create(String.format("/orders/%s/orderitems/%s", orderId, orderItemId))).build();
    }

    @PutMapping("/{orderId}/orderitems/{orderItemId}")
    public ResponseEntity updateItem(@PathVariable long orderItemId, @RequestParam int count) {
        orderService.updateItem(orderItemId, count);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{orderId}/orderitems/{orderItemId}")
    public ResponseEntity removeItem(@PathVariable long orderItemId) {
        orderService.removeItem(orderItemId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void orderNotFoundException(OrderNotFoundException ex) {

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void orderItemNotFoundException(OrderItemNotFoundException ex) {

    }


    private OrderResponse getOrderResponse(Order order) {
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemResponses.add(getOrderItemResponse(orderItem));
        }
        return new OrderResponse(order, orderItemResponses);
    }

    private OrderItemResponse getOrderItemResponse(OrderItem orderItem) {
        Product product = productClient.get(orderItem.getProductId());
        return new OrderItemResponse(orderItem, product);

    }

}
