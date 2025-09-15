package com.springboot.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.todo.entity.SubTask;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    List<SubTask> findByTlUsername(String username);

    List<SubTask> findByMemberUsername(String username);
    
    List<SubTask> findByProjectId(Long projectId);
    
    List<SubTask> findByCreatedByUsername(String username);
}