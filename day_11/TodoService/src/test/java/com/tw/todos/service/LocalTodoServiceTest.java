package com.tw.todos.service;

import com.tw.todos.model.Todo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author yangyi
 * @date 30/07/2018 4:25 PM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LocalTodoServiceTest {

//    @Autowired
//    private TodoService todoService;
//
//    @Before
//    public void setUp() throws Exception {
//     //   todoService.clear();
//    }
//
//    private Todo addTodo() {
//        return todoService.add(new Todo("todo text", false));
//    }
//
//    @Test
//    public void shouldInitEmptyTodosCorrectly() throws Exception {
////        assertThat(todoService.getTodos().size()).isEqualTo(0);
//    }
//
//    @Test
//    public void shouldAddTodoCorrectly() throws Exception {
//        // when
//        Todo todo = addTodo();
//        // then
////        assertThat(todoService.getTodos().size()).isEqualTo(1);
////        assertThat(todoService.getTodos().get(0)).isEqualTo(todo);
//        assertThat(todo.getId()).isGreaterThanOrEqualTo(0);
//        assertThat(todo.getText()).isEqualTo("todo text");
//        assertThat(todo.isCompleted()).isFalse();
//    }
//
//
//    @Test
//    public void shouldGetAllTodosCorrectly() throws Exception {
//        // given
//        addTodo();
//        addTodo();
//        // when
//        List<Todo> todos = todoService.getTodos();
//        // then
//        assertThat(todos.size()).isEqualTo(2);
//    }
//
//    @Test
//    public void shouldDeleteTodoCorrectly() throws Exception {
//        // given
//        Todo todo1 = addTodo();
//        Todo todo2 = addTodo();
//        // when
//        todoService.delete(todo1.getId());
//        // then
//        assertThat(todoService.getTodos().size()).isEqualTo(1);
//        assertThat(todoService.getTodos().get(0).getId()).isEqualTo(todo2.getId());
//    }
//
//
//    @Test
//    public void shouldUpdateTodoCorrectly() throws Exception {
//        // given
//        Todo todo = addTodo();
//        Todo todoToUpdate = new Todo("new text", true);
//        todoToUpdate.setId(todo.getId());
//        // when
//        boolean updateSuccess = todoService.update(todoToUpdate);
//        // then
//        assertThat(updateSuccess).isTrue();
//        assertThat(todoService.getTodos().size()).isEqualTo(1);
//        assertThat(todoService.getTodos().get(0).getText()).isEqualTo("new text");
//        assertThat(todoService.getTodos().get(0).isCompleted()).isTrue();
//    }
//
//    @Test
//    public void shouldUpdateFailedWhenIdNotExist() throws Exception {
//        // given
//        Todo todo = addTodo();
//        Todo todoToUpdate = new Todo("new text", true);
//        todoToUpdate.setId(todo.getId() + 1);
//        // when
//        boolean updateSuccess = todoService.update(todoToUpdate);
//        // then
//        assertThat(updateSuccess).isFalse();
//        assertThat(todoService.getTodos().size()).isEqualTo(1);
//        assertThat(todoService.getTodos().get(0).getText()).isNotEqualTo("new text");
//        assertThat(todoService.getTodos().get(0).isCompleted()).isFalse();
//    }

}