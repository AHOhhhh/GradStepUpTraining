package com.thoughtworks.mall_order.restService;

import com.thoughtworks.mall_order.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("product-client")
public interface ProductClient {

    @RequestMapping(
            method= RequestMethod.GET,
            value="/products/{id}")
    Product get(@PathVariable(name = "id") long id);
}
