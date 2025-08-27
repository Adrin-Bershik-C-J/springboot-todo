package com.springboot.todo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.springboot.todo.dto.ToDoRequestDTO;
import com.springboot.todo.dto.ToDoResponseDTO;
import com.springboot.todo.service.ToDoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    // Create task
    @PostMapping
    public ResponseEntity<ToDoResponseDTO> createTask(
            @Valid @RequestBody ToDoRequestDTO todoDto,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toDoService.createTask(todoDto, authentication.getName()));
    }

    // Get single task by id
    @GetMapping("/{id}")
    public ResponseEntity<ToDoResponseDTO> getTaskById(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(toDoService.getTaskById(id, authentication.getName()));
    }

    // Get all tasks with pagination & sorting
    @GetMapping
    public ResponseEntity<Page<ToDoResponseDTO>> getAllTasks(
            Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(toDoService.getAllTasks(authentication.getName(), pageable));
    }

    // Update a task
    @PatchMapping("/{id}")
    public ResponseEntity<ToDoResponseDTO> updateTask(
            @PathVariable Long id,
            @RequestBody ToDoRequestDTO updated,
            Authentication authentication) {
        return ResponseEntity.ok(toDoService.updateTask(id, updated, authentication.getName()));
    }

    // Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {
        toDoService.deleteTask(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
