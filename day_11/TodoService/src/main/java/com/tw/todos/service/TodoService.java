package com.tw.todos.service;

import com.tw.todos.model.Todo;

import java.util.List;

/**
 * @author yangyi
 * @date 30/07/2018 4:24 PM
 */
public interface TodoService {

    Todo add(Todo todo);

    List<Todo> getTodos();

    void clear();

    void delete(int id);

    boolean update(Todo todo);
}
