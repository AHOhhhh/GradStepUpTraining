package com.thoughtworks.market.controller;

import com.thoughtworks.market.entity.Product;
import com.thoughtworks.market.entity.RoughProduct;
import com.thoughtworks.market.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarketController {

    @Autowired
    private MarketService localMarketService;


    @GetMapping("/products")
    public List<RoughProduct> filterProducts(
            @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(value = "brand", defaultValue = "") String brand,
            @RequestParam(value = "maxPrice", defaultValue = "0") float maxPrice,
            @RequestParam(value = "minPrice", defaultValue = "0") float minPrice) {

        return  localMarketService.get(category,brand,maxPrice,minPrice);
    }


//    @GetMapping("/products/all")
//    public List<RoughProduct> getAllProducts(
//            @RequestParam(required = false) Integer page,
//            @RequestParam(required = false) Boolean asc) {
//     return     localMarketService.getAllProducts(page,asc);
//    }
//
//    @GetMapping("/products/part")
//    public List<RoughProduct> filterProducts(
//            @RequestParam String category,
//            @RequestParam String brand,
//            @RequestParam float minPrice,
//            @RequestParam float maxPrice,
//            @RequestParam(required = false,defaultValue = "false") Boolean asc) {
//        return localMarketService.filterProductsByConditions(category, brand, minPrice, maxPrice, asc);
//
//    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable int id) {
        return localMarketService.getProductById(id);
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return localMarketService.addProduct(product);
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product product) {
        return localMarketService.updateProduct(id, product);
    }

}
