package com.springboot.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.todo.dto.RegisterRequestDTO;
import com.springboot.todo.enums.Role;
import com.springboot.todo.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(
            @RequestBody RegisterRequestDTO request,
            @RequestParam Role role) {

        String message = adminService.createUser(request, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
