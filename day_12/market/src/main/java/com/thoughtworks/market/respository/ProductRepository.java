package com.thoughtworks.market.respository;

import com.thoughtworks.market.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>, CustomRepository {

    public abstract List<Product> findByCategoryAndBrandAndPriceBetweenOrderByPrice(String category, String brand, float minPrice, float maxPrice);

    public abstract List<Product> findByCategoryAndBrandAndPriceBetweenOrderByPriceDesc(String category, String brand, float minPrice, float maxPrice);

}
