package com.springboot.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.todo.dto.RegisterRequestDTO;
import com.springboot.todo.entity.User;
import com.springboot.todo.enums.Role;
import com.springboot.todo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
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
}
