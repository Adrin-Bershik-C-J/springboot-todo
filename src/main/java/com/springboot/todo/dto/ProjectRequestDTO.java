package com.springboot.todo.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectRequestDTO {
    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    @NotBlank(message = "TL username is required")
    private String tlUsername;

    @NotEmpty(message = "At least one member username must be provided")
    private List<String> memberUsernames;
}
