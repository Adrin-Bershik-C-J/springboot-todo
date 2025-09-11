package com.springboot.todo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.todo.dto.UserResponseDTO;
import com.springboot.todo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getName(), user.getRole().name()))
                .toList();
    }
}