package com.springboot.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import com.springboot.todo.dto.RegisterRequestDTO;
import com.springboot.todo.dto.ProjectResponseDTO;
import com.springboot.todo.dto.SubTaskResponseDTO;
import com.springboot.todo.entity.Project;
import com.springboot.todo.entity.SubTask;
import com.springboot.todo.entity.User;
import com.springboot.todo.enums.Role;
import com.springboot.todo.repository.ProjectRepository;
import com.springboot.todo.repository.SubTaskRepository;
import com.springboot.todo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SubTaskRepository subTaskRepository;
    private final PasswordEncoder passwordEncoder;

    public String createUser(RegisterRequestDTO request, Role role) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = new User(
                request.getName(),
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                role);

        userRepository.save(user);

        return role + " user created successfully";
    }

    public List<ProjectResponseDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::mapProjectToResponse)
                .toList();
    }

    public List<SubTaskResponseDTO> getAllSubTasks() {
        List<SubTask> subTasks = subTaskRepository.findAll();
        return subTasks.stream()
                .map(this::mapSubTaskToResponse)
                .toList();
    }

    private ProjectResponseDTO mapProjectToResponse(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getDueDate(),
                project.getManager().getUsername(),
                project.getTl().getUsername(),
                project.getMembers().stream()
                        .map(User::getUsername)
                        .toList()
        );
    }

    private SubTaskResponseDTO mapSubTaskToResponse(SubTask subTask) {
        return new SubTaskResponseDTO(
                subTask.getId(),
                subTask.getName(),
                subTask.getDescription(),
                subTask.getDueDate(),
                subTask.getStatus().name(),
                subTask.getProject().getId(),
                subTask.getProject().getName(),
                subTask.getTl().getUsername(),
                subTask.getMember().getUsername()
        );
    }
}
