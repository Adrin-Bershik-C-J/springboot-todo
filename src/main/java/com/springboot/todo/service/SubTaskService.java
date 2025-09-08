package com.springboot.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.todo.dto.SubTaskRequestDTO;
import com.springboot.todo.dto.SubTaskResponseDTO;
import com.springboot.todo.entity.Project;
import com.springboot.todo.entity.SubTask;
import com.springboot.todo.entity.User;
import com.springboot.todo.enums.Status;
import com.springboot.todo.exception.ResourceNotFoundException;
import com.springboot.todo.repository.ProjectRepository;
import com.springboot.todo.repository.SubTaskRepository;
import com.springboot.todo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubTaskService {
    private final SubTaskRepository subTaskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public SubTaskResponseDTO createSubTask(SubTaskRequestDTO requestDTO) {
        Project project = projectRepository.findById(requestDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User tl = userRepository.findById(requestDTO.getTlId())
                .orElseThrow(() -> new ResourceNotFoundException("TL not found"));

        User member = userRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        SubTask subTask = new SubTask();
        subTask.setName(requestDTO.getName());
        subTask.setDescription(requestDTO.getDescription());
        subTask.setDueDate(requestDTO.getDueDate());
        subTask.setProject(project);
        subTask.setTl(tl);
        subTask.setMember(member);
        subTask.setStatus(Status.NOT_STARTED);

        SubTask saved = subTaskRepository.save(subTask);
        return mapToResponse(saved);
    }

    public List<SubTaskResponseDTO> getSubTasksByTL(String tlUsername) {
        return subTaskRepository.findByTlUsername(tlUsername)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<SubTaskResponseDTO> getSubTasksByMember(String memberUsername) {
        return subTaskRepository.findByMemberUsername(memberUsername)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SubTaskResponseDTO updateStatus(Long subTaskId, Status newStatus) {
        SubTask subTask = subTaskRepository.findById(subTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Sub-task not found"));

        subTask.setStatus(newStatus);
        SubTask updated = subTaskRepository.save(subTask);

        return mapToResponse(updated);
    }

    private SubTaskResponseDTO mapToResponse(SubTask subTask) {
        return new SubTaskResponseDTO(
                subTask.getId(),
                subTask.getName(),
                subTask.getDescription(),
                subTask.getDueDate(),
                subTask.getStatus().name(),
                subTask.getProject().getId(),
                subTask.getTl().getUsername(),
                subTask.getMember().getUsername());
    }
}
