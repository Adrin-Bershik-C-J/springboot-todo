package com.springboot.todo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;
}
