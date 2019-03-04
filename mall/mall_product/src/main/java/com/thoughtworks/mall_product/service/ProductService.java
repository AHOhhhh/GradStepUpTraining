package com.thoughtworks.mall_product.service;

import com.thoughtworks.mall_product.entity.Product;
import com.thoughtworks.mall_product.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thoughtworks.mall_product.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public long add(Product product) {
        return productRepository.save(product).getId();
    }

    public Product get(long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public Product update(long id, Product product) {
        Product oldProduct = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        product.setId(oldProduct.getId());
        return productRepository.save(product);
    }

    public void remove(long id) {
        productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.deleteById(id);
    }
}
