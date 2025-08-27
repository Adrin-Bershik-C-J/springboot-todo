package com.springboot.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.todo.dto.ToDoRequestDTO;
import com.springboot.todo.dto.ToDoResponseDTO;
import com.springboot.todo.entity.ToDo;
import com.springboot.todo.entity.User;
import com.springboot.todo.exception.ResourceNotFoundException;
import com.springboot.todo.repository.ToDoRepository;
import com.springboot.todo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;

    // Create Task
    public ToDoResponseDTO createTask(ToDoRequestDTO newTask, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ToDo todo = new ToDo();
        todo.setTitle(newTask.getTitle());
        todo.setDescription(newTask.getDescription());
        todo.setDueDate(newTask.getDueDate());
        todo.setPriority(newTask.getPriority());
        todo.setStatus(newTask.getStatus());
        todo.setUser(user);

        ToDo saved = toDoRepository.save(todo);

        return mapToResponse(saved);
    }

    // Get Single Task
    public ToDoResponseDTO getTaskById(Long id, String username) {
        ToDo task = toDoRepository.findById(id)
                .filter(todo -> todo.getUser().getUsername().equals(username))
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or not accessible"));

        return mapToResponse(task);
    }

    // Get All Tasks (Paginated + Sorted)
    public Page<ToDoResponseDTO> getAllTasks(String username, Pageable pageable) {
        return toDoRepository.findByUserUsername(username, pageable)
                .map(this::mapToResponse);
    }

    // Update Task
    public ToDoResponseDTO updateTask(Long id, ToDoRequestDTO updateDTO, String username) {
        ToDo task = toDoRepository.findById(id)
                .filter(todo -> todo.getUser().getUsername().equals(username))
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or not accessible"));

        if (updateDTO.getTitle() != null) task.setTitle(updateDTO.getTitle());
        if (updateDTO.getDescription() != null) task.setDescription(updateDTO.getDescription());
        if (updateDTO.getPriority() != null) task.setPriority(updateDTO.getPriority());
        if (updateDTO.getDueDate() != null) task.setDueDate(updateDTO.getDueDate());
        if (updateDTO.getStatus() != null) task.setStatus(updateDTO.getStatus());

        ToDo updated = toDoRepository.save(task);
        return mapToResponse(updated);
    }

    // Delete Task
    public void deleteTask(Long id, String username) {
        ToDo task = toDoRepository.findById(id)
                .filter(todo -> todo.getUser().getUsername().equals(username))
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or not accessible"));
        toDoRepository.delete(task);
    }

    // Mapper
    private ToDoResponseDTO mapToResponse(ToDo todo) {
        return new ToDoResponseDTO(
                todo.getTitle(),
                todo.getDescription(),
                todo.getPriority(),
                todo.getStatus(),
                todo.getDueDate()
        );
    }
}
