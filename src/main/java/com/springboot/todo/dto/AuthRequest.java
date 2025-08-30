package com.springboot.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}