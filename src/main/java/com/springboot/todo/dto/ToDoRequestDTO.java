package com.springboot.todo.dto;

import java.time.LocalDateTime;

import com.springboot.todo.enums.Priority;
import com.springboot.todo.enums.Status;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ToDoRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title can be at most 100 characters")
    private String title;

    @Size(max = 500, message = "Description can be at most 500 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDateTime dueDate;

}
