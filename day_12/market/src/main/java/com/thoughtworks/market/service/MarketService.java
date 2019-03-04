package com.thoughtworks.market.service;

import com.thoughtworks.market.entity.Product;
import com.thoughtworks.market.entity.RoughProduct;
import com.thoughtworks.market.respository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarketService {

    @Autowired
    private ProductRepository productRespository;
    private int pageSize = 5;

    public List<RoughProduct> getAllProducts(Integer page, Boolean asc) {

        PageRequest pageable = PageRequest.of(page == null ? 0 : page, pageSize, asc == null || asc ? Sort.Direction.ASC : Sort.Direction.DESC, "price");
        Page<Product> products = productRespository.findAll(pageable);
        return products.getContent().stream().map(RoughProduct::new).collect(Collectors.toList());
    }


    public List<RoughProduct> get(String category, String brand, float maxPrice, float minPrice) {
        List<Product> products = productRespository.fileterProducts(category, brand, maxPrice, minPrice);
        return  products.stream().map(RoughProduct::new).collect(Collectors.toList());
    }


//    @Override
//    public List<RoughProduct> filterProductsByConditions(String category, String brand, Float minPrice, Float maxPrice, Boolean asc) {
//        List<Product> products;
//        if (asc == null || asc) {
//            products = productRespository.findByCategoryAndBrandAndPriceBetweenOrderByPrice(category, brand, minPrice, maxPrice);
//        } else {
//            products = productRespository.findByCategoryAndBrandAndPriceBetweenOrderByPriceDesc(category, brand, minPrice, maxPrice);
//        }
//        return products.stream().map(RoughProduct::new).collect(Collectors.toList());
//    }


    public Product getProductById(int id) {
        Optional<Product> product = productRespository.findById(id);
        return product.orElse(null);

    }


    public Product addProduct(Product product) {
        System.out.println(product.getName());
        return productRespository.save(product);

    }

    public Product updateProduct(int id, Product product) {

        Optional<Product> updateProduct = productRespository.findById(id);
        if (updateProduct.isPresent()) {
            product.setId(id);
            return productRespository.save(product);
        }
        return null;
    }
}
