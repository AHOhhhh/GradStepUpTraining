package com.thoughtworks.mall_order.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.thoughtworks.mall_order.controller.request.AddOrderItemRequest;
import com.thoughtworks.mall_order.controller.request.CreateOrderItemRequest;
import com.thoughtworks.mall_order.controller.request.CreateOrderRequest;
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

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@DatabaseSetup("classpath:/initOrderData.xml")
@DatabaseSetup("classpath:/initOrderItemData.xml")
public class OrderControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void getAll() {
        given().port(port)
                .when().header("userId", 1).get("/orders")
                .then().body("id", contains(1));

    }

    @Test
    public void get() {
        given().port(port)
                .when().get("/orders/1")
                .then().statusCode(200).body("userId", equalTo(1));
    }

    @Test
    @ExpectedDatabase(value = "classpath:/expectedOrderData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    @ExpectedDatabase(value = "classpath:/expectedOrderItemAddOrderData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() {
        //given
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        List<CreateOrderItemRequest> createOrderItemRequestList = new ArrayList<>();
        CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest();
        createOrderItemRequest.setProductId(3);
        createOrderItemRequest.setProductCount(3);
        createOrderItemRequestList.add(createOrderItemRequest);
        createOrderRequest.setCreateOrderItemRequestList(createOrderItemRequestList);

        given().port(port)
                .when().header("userId",1).contentType(ContentType.JSON).body(createOrderRequest).post("/orders")
                .then().statusCode(201);
    }

    @Test
    @ExpectedDatabase(value = "classpath:/expectedOrderItemAddItemData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addItem() {
        AddOrderItemRequest addOrderItemRequest=new AddOrderItemRequest();
        addOrderItemRequest.setProductCount(10);
        addOrderItemRequest.setProductId(3);
        given().port(port)
                .when().contentType(ContentType.JSON).body(addOrderItemRequest).post("/orders/1/orderitems")
                .then().statusCode(201);
    }

    @Test
    @ExpectedDatabase(value = "classpath:/expectedOrderItemUpdateItemData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void updateItem() {
        int count=10;
        long orderItemId=1;
        given().port(port)
                .when().put(String.format("/orders/1/orderitems/%s?count=%s",orderItemId,count))
                .then().statusCode(204);

    }

    @Test
    @ExpectedDatabase(value = "classpath:/expectedOrderItemDeleteItemData.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)

    public void removeItem() {
        long orderItemId=1;
        given().port(port)
                .when().delete(String.format("/orders/1/orderitems/%s",orderItemId))
                .then().statusCode(204);
    }


}