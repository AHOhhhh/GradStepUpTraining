package com.tw.todos.respository;

import com.tw.todos.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;





public interface TodoRespository extends JpaRepository<Todo, Integer> {
}
