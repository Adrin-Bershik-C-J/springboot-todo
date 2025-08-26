package com.springboot.todo.dto;

import java.time.LocalDateTime;

import com.springboot.todo.enums.Priority;
import com.springboot.todo.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoResponseDTO {

    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDateTime dueDate;
    
}
