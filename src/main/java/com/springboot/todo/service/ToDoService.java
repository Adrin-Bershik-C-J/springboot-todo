package com.springboot.todo.service;

import org.springframework.stereotype.Service;

import com.springboot.todo.dto.ToDoRequestDTO;
import com.springboot.todo.dto.ToDoResponseDTO;
import com.springboot.todo.entity.ToDo;
import com.springboot.todo.exception.ResourceNotFoundException;
import com.springboot.todo.repository.ToDoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final ToDoRepository toDoRepository;

    public ToDoResponseDTO createTask(ToDoRequestDTO newTask) {

        ToDo todo = new ToDo();

        todo.setTitle(newTask.getTitle());
        todo.setDescription(newTask.getDescription());
        todo.setDueDate(newTask.getDueDate());
        todo.setPriority(newTask.getPriority());
        todo.setStatus(newTask.getStatus());

        ToDo saved = toDoRepository.save(todo);

        return new ToDoResponseDTO(
                saved.getTitle(),
                saved.getDescription(),
                saved.getPriority(),
                saved.getStatus(),
                saved.getDueDate());
    }

    public ToDoResponseDTO getTaskById(Long id) {
        ToDo task = toDoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        return new ToDoResponseDTO(
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate());
    }

    public ToDoResponseDTO updateTask(Long id, ToDoRequestDTO updateDTO) {
        ToDo task = toDoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        if (updateDTO.getTitle() != null) {
            task.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getDescription() != null) {
            task.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getPriority() != null) {
            task.setPriority(updateDTO.getPriority());
        }
        if (updateDTO.getDueDate() != null) {
            task.setDueDate(updateDTO.getDueDate());
        }
        if (updateDTO.getStatus() != null) {
            task.setStatus(updateDTO.getStatus());
        }
        ToDo updated = toDoRepository.save(task);
        return new ToDoResponseDTO(updated.getTitle(), updated.getDescription(), updated.getPriority(),
                updated.getStatus(), updated.getDueDate());
    }

    public void deleteTask(Long id) {
        ToDo task = toDoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id:" + id));
        toDoRepository.delete(task);
    }
}
