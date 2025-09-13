package com.springboot.todo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.todo.dto.ProjectRequestDTO;
import com.springboot.todo.dto.ProjectResponseDTO;
import com.springboot.todo.dto.UserResponseDTO;
import com.springboot.todo.entity.Project;
import com.springboot.todo.entity.SubTask;
import com.springboot.todo.entity.User;
import com.springboot.todo.enums.Role;
import com.springboot.todo.exception.ResourceNotFoundException;
import com.springboot.todo.repository.ProjectRepository;
import com.springboot.todo.repository.SubTaskRepository;
import com.springboot.todo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SubTaskRepository subTaskRepository;

    public ProjectResponseDTO createProject(ProjectRequestDTO dto, String managerUsername) {
        User manager = userRepository.findByUsername(managerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

        User tl = userRepository.findByUsername(dto.getTlUsername())
                .orElseThrow(() -> new ResourceNotFoundException("TL not found"));

        if (tl.getRole() != Role.TL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provided TL username does not belong to a user with TL role");
        }

        List<User> members = dto.getMemberUsernames().stream()
                .map(username -> userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + username)))
                .toList();
        
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

    public ProjectResponseDTO addMember(Long projectId, String memberUsername, String managerUsername) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // ✅ Ensure the authenticated manager is the actual project manager
        if (!project.getManager().getUsername().equals(managerUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not the manager of this project");
        }

        User member = userRepository.findByUsername(memberUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        if (member.getRole() != Role.MEMBER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a Member");
        }

        if (project.getMembers().contains(member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member already added");
        }

        project.getMembers().add(member);
        Project updated = projectRepository.save(project);

        return mapToResponse(updated);
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

    public ProjectResponseDTO updateProject(Long projectId, ProjectRequestDTO dto, String managerUsername) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getManager().getUsername().equals(managerUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the manager of this project");
        }

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setDueDate(dto.getDueDate());

        Project updated = projectRepository.save(project);
        return mapToResponse(updated);
    }



    public List<UserResponseDTO> getProjectMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        List<UserResponseDTO> allMembers = new java.util.ArrayList<>();
        
        // Add manager
        allMembers.add(new UserResponseDTO(project.getManager().getId(), project.getManager().getUsername(), 
                project.getManager().getName(), project.getManager().getRole().name()));
        
        // Add TL
        allMembers.add(new UserResponseDTO(project.getTl().getId(), project.getTl().getUsername(), 
                project.getTl().getName(), project.getTl().getRole().name()));
        
        // Add all project members
        project.getMembers().forEach(user -> 
            allMembers.add(new UserResponseDTO(user.getId(), user.getUsername(), user.getName(), user.getRole().name()))
        );
        
        return allMembers;
    }

    public List<ProjectResponseDTO> getProjectsByTL(String tlUsername) {
        List<Project> projects = projectRepository.findByTlUsername(tlUsername);
        return projects.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getName(), user.getRole().name()))
                .toList();
    }

    public void deleteProject(Long projectId, String managerUsername) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getManager().getUsername().equals(managerUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the manager of this project");
        }

        // Delete all sub-tasks associated with this project first
        List<SubTask> subTasks = subTaskRepository.findByProjectId(projectId);
        subTaskRepository.deleteAll(subTasks);

        // Now delete the project
        projectRepository.delete(project);
    }
}
