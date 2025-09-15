package com.springboot.todo.dto;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String name;
    private String username;
    private String password;
}