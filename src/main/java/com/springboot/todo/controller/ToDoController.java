package com.springboot.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public ResponseEntity<ToDoResponseDTO> createTask(@Valid @RequestBody ToDoRequestDTO todoDto) {
        ToDoResponseDTO response = toDoService.createTask(todoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(toDoService.getTaskById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ToDoResponseDTO> updateTask(@PathVariable Long id, @RequestBody ToDoRequestDTO updated) {
        return ResponseEntity.ok(toDoService.updateTask(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        toDoService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
