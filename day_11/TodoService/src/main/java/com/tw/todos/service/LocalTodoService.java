package com.tw.todos.service;

import com.tw.todos.model.Todo;
import com.tw.todos.respository.TodoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yangyi
 * @date 30/07/2018 4:25 PM
 */
@Service
public class LocalTodoService implements TodoService {

    @Autowired
    private TodoRespository respository;

    @Override
    public Todo add(Todo todo) {
        respository.save(todo);
        return todo;
    }

    @Override
    public List<Todo> getTodos() {
        return respository.findAll();
    }

    @Override
    public void clear() {
        respository.deleteAll();
    }

    @Override
    public void delete(int id) {
        Optional<Todo> deleteTodo = respository.findById(id);
        if (deleteTodo.isPresent()) {
            respository.deleteById(id);
        }
    }

    @Override
    public boolean update(Todo todo) {

        Optional<Todo> byId = respository.findById(todo.getId());
        if (!byId.isPresent()) {
            return false;
        }
        Todo updateTodo = byId.get();
        updateTodo.setText(todo.getText());
        updateTodo.setCompleted(todo.isCompleted());
        respository.save(updateTodo);
        return true;
    }

    public List<Todo> filterTodos(boolean completed) {
        return respository.findAll().stream().filter(todo -> todo.isCompleted() == completed).collect(Collectors.toList());
    }
}
