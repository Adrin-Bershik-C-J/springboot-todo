package com.springboot.todo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubTaskResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate dueDate;
    private String status;
    private Long projectId;
    private String tlUsername;
    private String memberUsername;
}
