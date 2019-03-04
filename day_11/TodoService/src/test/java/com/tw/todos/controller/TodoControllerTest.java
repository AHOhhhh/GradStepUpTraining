package com.tw.todos.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author yangyi
 * @date 30/07/2018 4:16 PM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TodoControllerTest {

    @Autowired
    private TodoController todosController;


    @Test
    public void shouldReturnHelloCorrectly() throws Exception {
     //   String result = todosController.hello();
    //    assertThat(result).isEqualTo("hello world");
    }


    // need Mock to test controller, waiting for learning

    @Test
    public void shouldAdd() throws Exception {
        // given

        // when

        // then
    }

    @Test
    public void shouldGetAll() throws Exception {
        // given

        // when

        // then
    }

    @Test
    public void shouldDelete() throws Exception {
        // given

        // when

        // then
    }

    @Test
    public void shouldUpdate() throws Exception {
        // given

        // when

        // then
    }
}