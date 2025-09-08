package com.springboot.todo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("/tl/{username}")
    public ResponseEntity<List<SubTaskResponseDTO>> getSubTasksByTL(
            @PathVariable String username) {

        List<SubTaskResponseDTO> subtasks = subTaskService.getSubTasksByTL(username);
        return ResponseEntity.ok(subtasks);
    }

    // Member fetches subtasks assigned to them
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/member/{username}")
    public ResponseEntity<List<SubTaskResponseDTO>> getSubTasksByMember(
            @PathVariable String username) {

        List<SubTaskResponseDTO> subtasks = subTaskService.getSubTasksByMember(username);
        return ResponseEntity.ok(subtasks);
    }

    // TL or Member updates status of a sub-task
    @PreAuthorize("hasAnyRole('TL', 'MEMBER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<SubTaskResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status) {

        SubTaskResponseDTO updated = subTaskService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}
