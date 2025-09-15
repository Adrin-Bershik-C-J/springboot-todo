package com.springboot.todo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.todo.dto.ProjectResponseDTO;
import com.springboot.todo.dto.SubTaskResponseDTO;
import com.springboot.todo.dto.UserCreateDTO;
import com.springboot.todo.dto.UserResponseDTO;
import com.springboot.todo.enums.Role;
import com.springboot.todo.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/projects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        return ResponseEntity.ok(adminService.getAllProjects());
    }

    @GetMapping("/subtasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SubTaskResponseDTO>> getAllSubTasks() {
        return ResponseEntity.ok(adminService.getAllSubTasks());
    }

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO userCreateDTO, @RequestParam Role role) {
        return ResponseEntity.ok(adminService.createUser(userCreateDTO, role));
    }

    @DeleteMapping("/users/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        adminService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}