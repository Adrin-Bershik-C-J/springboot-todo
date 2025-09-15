package com.springboot.todo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.todo.dto.SubTaskRequestDTO;
import com.springboot.todo.dto.SubTaskResponseDTO;
import com.springboot.todo.entity.Project;
import com.springboot.todo.entity.SubTask;
import com.springboot.todo.entity.User;
import com.springboot.todo.enums.Role;
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

        public SubTaskResponseDTO createSubTask(SubTaskRequestDTO requestDTO, String creatorUsername) {
                Project project = projectRepository.findById(requestDTO.getProjectId())
                                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

                User creator = userRepository.findByUsername(creatorUsername)
                                .orElseThrow(() -> new ResourceNotFoundException("Creator not found"));

                User assignee = userRepository.findByUsername(requestDTO.getAssigneeUsername())
                                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));

                // Validate if assignee is part of the project (manager, TL, or member)
                boolean isProjectMember = project.getManager().getUsername().equals(assignee.getUsername()) ||
                                project.getTl().getUsername().equals(assignee.getUsername()) ||
                                project.getMembers().stream().anyMatch(m -> m.getUsername().equals(assignee.getUsername()));
                
                if (!isProjectMember) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignee is not part of the project");
                }

                if (requestDTO.getDueDate().isAfter(project.getDueDate())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Subtask due date must be before project due date");
                }

                SubTask subTask = new SubTask();
                subTask.setName(requestDTO.getName());
                subTask.setDescription(requestDTO.getDescription());
                subTask.setDueDate(requestDTO.getDueDate());
                subTask.setProject(project);
                subTask.setTl(project.getTl());
                subTask.setMember(assignee);
                subTask.setCreatedBy(creator);
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

        public Page<SubTaskResponseDTO> getAllSubTasksForTLProjects(String tlUsername, Pageable pageable) {
                List<Project> tlProjects = projectRepository.findByTlUsername(tlUsername);
                
                List<SubTaskResponseDTO> allSubTasks = tlProjects.stream()
                                .flatMap(project -> subTaskRepository.findByProjectId(project.getId()).stream())
                                .map(this::mapToResponse)
                                .toList();
                
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), allSubTasks.size());
                List<SubTaskResponseDTO> pageContent = start < allSubTasks.size() ? allSubTasks.subList(start, end) : List.of();
                
                return new PageImpl<>(pageContent, pageable, allSubTasks.size());
        }

        public List<SubTaskResponseDTO> getSubTasksByMember(String memberUsername) {
                return subTaskRepository.findByMemberUsername(memberUsername)
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        public Page<SubTaskResponseDTO> getSubTasksByManager(String managerUsername, Pageable pageable) {
                User manager = userRepository.findByUsername(managerUsername)
                                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
                
                List<Project> managerProjects = projectRepository.findByManagerUsername(managerUsername);
                
                List<SubTaskResponseDTO> allSubTasks = managerProjects.stream()
                                .flatMap(project -> subTaskRepository.findByProjectId(project.getId()).stream())
                                .map(this::mapToResponse)
                                .toList();
                
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), allSubTasks.size());
                List<SubTaskResponseDTO> pageContent = start < allSubTasks.size() ? allSubTasks.subList(start, end) : List.of();
                
                return new PageImpl<>(pageContent, pageable, allSubTasks.size());
        }

        public Page<SubTaskResponseDTO> getSubTasksCreatedBy(String creatorUsername, Pageable pageable) {
                List<SubTaskResponseDTO> allSubTasks = subTaskRepository.findByCreatedByUsername(creatorUsername)
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
                
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), allSubTasks.size());
                List<SubTaskResponseDTO> pageContent = start < allSubTasks.size() ? allSubTasks.subList(start, end) : List.of();
                
                return new PageImpl<>(pageContent, pageable, allSubTasks.size());
        }

        public SubTaskResponseDTO updateStatus(Long subTaskId, Status newStatus) {
                SubTask subTask = subTaskRepository.findById(subTaskId)
                                .orElseThrow(() -> new ResourceNotFoundException("Sub-task not found"));

                subTask.setStatus(newStatus);
                SubTask updated = subTaskRepository.save(subTask);

                return mapToResponse(updated);
        }

        public SubTaskResponseDTO updateSubTask(Long subTaskId, SubTaskRequestDTO requestDTO, String username) {
                SubTask subTask = subTaskRepository.findById(subTaskId)
                                .orElseThrow(() -> new ResourceNotFoundException("Sub-task not found"));

                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                // Manager can edit all sub-tasks from their projects, TL can only edit sub-tasks they created
                boolean canEdit = (user.getRole() == Role.MANAGER && subTask.getProject().getManager().getUsername().equals(username)) ||
                                (user.getRole() == Role.TL && subTask.getCreatedBy() != null && subTask.getCreatedBy().getUsername().equals(username));

                if (!canEdit) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to edit this sub-task");
                }

                User assignee = userRepository.findByUsername(requestDTO.getAssigneeUsername())
                                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));

                subTask.setName(requestDTO.getName());
                subTask.setDescription(requestDTO.getDescription());
                subTask.setDueDate(requestDTO.getDueDate());
                subTask.setMember(assignee);

                SubTask updated = subTaskRepository.save(subTask);
                return mapToResponse(updated);
        }

        public void deleteSubTask(Long subTaskId, String username) {
                SubTask subTask = subTaskRepository.findById(subTaskId)
                                .orElseThrow(() -> new ResourceNotFoundException("Sub-task not found"));

                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                // Admin can delete any sub-task, Manager can delete sub-tasks from their projects, TL can only delete sub-tasks they created
                boolean canDelete = user.getRole() == Role.ADMIN ||
                                (user.getRole() == Role.MANAGER && subTask.getProject().getManager().getUsername().equals(username)) ||
                                (user.getRole() == Role.TL && subTask.getCreatedBy() != null && subTask.getCreatedBy().getUsername().equals(username));

                if (!canDelete) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to delete this sub-task");
                }

                subTaskRepository.delete(subTask);
        }

        private SubTaskResponseDTO mapToResponse(SubTask subTask) {
                return new SubTaskResponseDTO(
                                subTask.getId(),
                                subTask.getName(),
                                subTask.getDescription(),
                                subTask.getDueDate(),
                                subTask.getStatus().name(),
                                subTask.getProject().getId(),
                                subTask.getProject().getName(),
                                subTask.getTl().getUsername(),
                                subTask.getMember().getUsername(),
                                subTask.getCreatedBy() != null ? subTask.getCreatedBy().getUsername() : "Unknown");
        }
}
