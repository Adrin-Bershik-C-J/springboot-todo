package com.springboot.todo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubTaskRequestDTO {
    @NotBlank(message = "Sub-task name is required")
    private String name;

    private String description;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "TL ID is required")
    private Long tlId;

    @NotNull(message = "Member ID is required")
    private Long memberId;
}
