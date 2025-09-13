package com.springboot.todo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.todo.dto.ProjectRequestDTO;
import com.springboot.todo.dto.ProjectResponseDTO;
import com.springboot.todo.dto.UserResponseDTO;
import com.springboot.todo.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProjectResponseDTO> createProject(
            @RequestBody @Valid ProjectRequestDTO requestDTO,
            Authentication authentication) {
        return ResponseEntity.ok(
                projectService.createProject(requestDTO, authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<ProjectResponseDTO>> getProjects(Authentication authentication) {
        return ResponseEntity.ok(
                projectService.getProjectsByManager(authentication.getName()));
    }

    @PostMapping("/add-member")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProjectResponseDTO> addMemberToProject(
            @RequestParam Long projectId,
            @RequestParam String memberUsername,
            Authentication authentication) {

        String managerUsername = authentication.getName();

        ProjectResponseDTO updatedProject = projectService.addMember(projectId, memberUsername, managerUsername);
        return ResponseEntity.ok(updatedProject);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long id,
            @RequestBody @Valid ProjectRequestDTO requestDTO,
            Authentication authentication) {
        return ResponseEntity.ok(
                projectService.updateProject(id, requestDTO, authentication.getName()));
    }



    @GetMapping("/{id}/members")
    @PreAuthorize("hasAnyRole('MANAGER', 'TL')")
    public ResponseEntity<List<UserResponseDTO>> getProjectMembers(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectMembers(id));
    }

    @GetMapping("/tl")
    @PreAuthorize("hasRole('TL')")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByTL(Authentication authentication) {
        return ResponseEntity.ok(projectService.getProjectsByTL(authentication.getName()));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('MANAGER', 'TL', 'ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(projectService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, Authentication authentication) {
        projectService.deleteProject(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

}
