package com.thoughtworks.mall_product.service;

import com.thoughtworks.mall_product.entity.Product;
import com.thoughtworks.mall_product.exception.ProductNotFoundException;
import com.thoughtworks.mall_product.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    private ProductService productService;

    @Before
    public void setUp() throws Exception {
        productService = new ProductService(productRepository);
    }

    @Test
    public void getAll() {

        //given
        List<Product> products = new ArrayList<>();
        products.add(new Product("1", "1", "1", 1));
        products.add(new Product("1", "1", "1", 1));
        products.add(new Product("1", "1", "1", 1));

        //when
        given(productRepository.findAll()).willReturn(products);

        //then
        List<Product> actual = productService.getAll();
        assertThat(actual.size()).isEqualTo(products.size());
    }

    @Test
    public void add() {
        //given
        Product product = new Product("1", "1", "1", 1);
        product.setId(1);

        //when
        given(productRepository.save(any(Product.class))).willReturn(product);

        //then
        long actualId = productService.add(product);
        assertThat(actualId).isEqualTo(1);


    }

    @Test
    public void get() {
        //given
        Product product = new Product("1", "1", "1", 1);
        product.setId(1);
        long id = 1;
        //when
        given(productRepository.findById(id)).willReturn(Optional.of(product));
        //then
        Product resultProduct = productService.get(id);
        assertThat(resultProduct.getId()).isEqualTo(id);
    }

    @Test(expected = ProductNotFoundException.class)
    public void get_not_found() {
        //given
        long id = 2;
        //when
        given(productRepository.findById(id)).willThrow(new ProductNotFoundException());
        //then
        Product resultProduct = productService.get(id);
    }

    @Test
    public void update() {
        //given
        Product product = new Product("1", "1", "1", 1);
        long id = 1;
        product.setId(id);
        //when
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).willReturn(product);

        //then
        Product acutalProduct = productService.update(id, product);
        assertThat(acutalProduct.getId()).isEqualTo(id);
    }

    @Test
    public void delete() {
        //given
        Product product = new Product();
        long id = 1;
        product.setId(id);

        //when
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        //then
        productService.remove(id);
        verify(productRepository, times(1)).deleteById(id);
    }
}