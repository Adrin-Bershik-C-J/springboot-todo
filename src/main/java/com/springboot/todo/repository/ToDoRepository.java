package com.springboot.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.todo.entity.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    Page<ToDo> findByUserUsername(String username, Pageable pageable);
}
