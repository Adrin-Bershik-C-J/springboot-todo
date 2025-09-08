package com.springboot.todo.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate dueDate;
    private String managerUsername;
    private String tlUsername;
    private List<String> memberUsernames;
}
