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

    @NotNull(message = "TL id is required")
    private Long tlId;

    @NotEmpty(message = "At least one member id must be provided")
    private List<Long> memberIds;
}
