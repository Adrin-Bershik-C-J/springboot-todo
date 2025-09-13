package com.springboot.todo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.todo.dto.ProjectResponseDTO;
import com.springboot.todo.dto.SubTaskResponseDTO;
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
}