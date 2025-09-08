package com.springboot.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.todo.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByManagerUsername(String username);
}
