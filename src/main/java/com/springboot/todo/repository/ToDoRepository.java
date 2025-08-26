package com.springboot.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.todo.entity.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

}
