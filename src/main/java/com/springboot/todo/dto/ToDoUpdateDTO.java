package com.springboot.todo.dto;

import java.time.LocalDateTime;

import com.springboot.todo.enums.Priority;
import com.springboot.todo.enums.Status;

import lombok.Data;

/** Partial update DTO — all fields optional */
@Data
public class ToDoUpdateDTO {
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDateTime dueDate;
}
