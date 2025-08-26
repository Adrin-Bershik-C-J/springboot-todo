package com.springboot.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.todo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
