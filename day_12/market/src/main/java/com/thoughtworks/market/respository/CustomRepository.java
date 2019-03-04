package com.thoughtworks.market.respository;

import com.thoughtworks.market.entity.Product;

import java.util.List;

public interface CustomRepository {
     List<Product> fileterProducts(String category,String brand, float maxPrice, float minPrice);
}
