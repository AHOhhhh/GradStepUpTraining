package com.tw.todos.controller;

import com.tw.todos.model.Todo;
import com.tw.todos.service.LocalTodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yangyi
 * @date 30/07/2018 4:16 PM
 */
@RestController
public class TodoController {

    @Autowired
    private LocalTodoService localTodoService;


    @GetMapping("/todos")
    public List<Todo> getAllTodos() {
        return localTodoService.getTodos();
    }

    @PostMapping("/add")
    public Todo add(@RequestBody Todo todo) {
        return localTodoService.add(todo);
    }

    @PostMapping("/update")
    public boolean update( @RequestBody Todo todo) {
        return localTodoService.update(todo);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam int id) {
        localTodoService.delete(id);
    }

    @GetMapping("/filter")
    public List<Todo> filter(@RequestParam boolean completed){
        return localTodoService.filterTodos(completed);
    }
}
