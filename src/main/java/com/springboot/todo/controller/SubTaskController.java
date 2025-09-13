package com.springboot.todo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.springboot.todo.dto.SubTaskRequestDTO;
import com.springboot.todo.dto.SubTaskResponseDTO;
import com.springboot.todo.enums.Status;
import com.springboot.todo.service.SubTaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subtasks")
@RequiredArgsConstructor
public class SubTaskController {

    private final SubTaskService subTaskService;

    // TL or Manager creates a sub-task
    @PreAuthorize("hasAnyRole('MANAGER', 'TL')")
    @PostMapping
    public ResponseEntity<SubTaskResponseDTO> createSubTask(
            @Valid @RequestBody SubTaskRequestDTO requestDTO) {

        SubTaskResponseDTO responseDTO = subTaskService.createSubTask(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // TL fetches subtasks assigned to them
    @PreAuthorize("hasRole('TL')")
    @GetMapping("/tl")
    public ResponseEntity<List<SubTaskResponseDTO>> getSubTasksByTL(Authentication authentication) {
        String tlUsername = authentication.getName();
        List<SubTaskResponseDTO> subtasks = subTaskService.getAllSubTasksForTLProjects(tlUsername);
        return ResponseEntity.ok(subtasks);
    }

    // Member fetches subtasks assigned to them
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/member")
    public ResponseEntity<List<SubTaskResponseDTO>> getMySubTasks(Authentication authentication) {
        String username = authentication.getName();
        List<SubTaskResponseDTO> tasks = subTaskService.getSubTasksByMember(username);
        return ResponseEntity.ok(tasks);
    }

    // Manager fetches subtasks by project
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager")
    public ResponseEntity<List<SubTaskResponseDTO>> getSubTasksByManager(Authentication authentication) {
        String managerUsername = authentication.getName();
        List<SubTaskResponseDTO> subtasks = subTaskService.getSubTasksByManager(managerUsername);
        return ResponseEntity.ok(subtasks);
    }

    // TL, Member, or Manager updates status of a sub-task
    @PreAuthorize("hasAnyRole('MANAGER', 'TL', 'MEMBER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<SubTaskResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status) {

        SubTaskResponseDTO updated = subTaskService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'TL')")
    @PutMapping("/{id}")
    public ResponseEntity<SubTaskResponseDTO> updateSubTask(
            @PathVariable Long id,
            @Valid @RequestBody SubTaskRequestDTO requestDTO,
            Authentication authentication) {
        SubTaskResponseDTO updated = subTaskService.updateSubTask(id, requestDTO, authentication.getName());
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'TL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long id, Authentication authentication) {
        subTaskService.deleteSubTask(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
