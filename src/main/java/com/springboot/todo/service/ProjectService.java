package com.springboot.todo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.todo.dto.ProjectRequestDTO;
import com.springboot.todo.dto.ProjectResponseDTO;
import com.springboot.todo.entity.Project;
import com.springboot.todo.entity.User;
import com.springboot.todo.exception.ResourceNotFoundException;
import com.springboot.todo.repository.ProjectRepository;
import com.springboot.todo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ProjectResponseDTO createProject(ProjectRequestDTO dto, String managerUsername) {
        User manager = userRepository.findByUsername(managerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        User tl = userRepository.findById(dto.getTlId())
                .orElseThrow(() -> new ResourceNotFoundException("TL not found"));

        List<User> members = userRepository.findAllById(dto.getMemberIds());
        if (members.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one valid member required");
        }

        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setDueDate(dto.getDueDate());
        project.setManager(manager);
        project.setTl(tl);
        project.setMembers(members);

        Project saved = projectRepository.save(project);

        return mapToResponse(saved);
    }

    private ProjectResponseDTO mapToResponse(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getDueDate(),
                project.getManager().getUsername(),
                project.getTl().getUsername(),
                project.getMembers().stream().map(User::getUsername).toList());
    }

    public List<ProjectResponseDTO> getProjectsByManager(String managerUsername) {
        List<Project> projects = projectRepository.findByManagerUsername(managerUsername);

        return projects.stream()
                .map(this::mapToResponse)
                .toList();
    }
}
