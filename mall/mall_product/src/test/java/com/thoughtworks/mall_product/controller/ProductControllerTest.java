package com.thoughtworks.mall_product.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.thoughtworks.mall_product.entity.Product;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@DatabaseSetup("classpath:/initProductData.xml")
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void getAll() {
        given()
                .port(port)
                .when()
                .get("/products")
                .then()
                .body("id", contains(1, 2));
    }

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value = "classpath:/expectedProductData.xml")
    public void add() {
        Product product = new Product();
        product.setName("Mr3");
        product.setPrice(3);
        product.setUnit("3");
        product.setImg("3");

        given().port(port)
                .when().contentType(ContentType.JSON).body(product).post("/products")
                .then().statusCode(201);
    }

    @Test
    public void get() {
        given().port(port)
                .when()
                .get("products/1")
                .then()
                .body("id", equalTo(1));
    }

    @Test
    public void update() {
        Product product = new Product();
        product.setName("Mr3");
        product.setPrice(3);
        product.setUnit("3");
        product.setImg("3");

        given().port(port)
                .when().contentType(ContentType.JSON).body(product).put("/products/1")
                .then().body("name",equalTo("Mr3"));

    }

}