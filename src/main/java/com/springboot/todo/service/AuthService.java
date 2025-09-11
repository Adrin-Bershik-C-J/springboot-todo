package com.springboot.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.todo.dto.AuthRequest;
import com.springboot.todo.dto.AuthResponse;
import com.springboot.todo.dto.RegisterRequestDTO;
import com.springboot.todo.dto.UserResponseDTO;
import com.springboot.todo.entity.User;
import com.springboot.todo.enums.Role;
import com.springboot.todo.exception.ResourceNotFoundException;
import com.springboot.todo.repository.UserRepository;
import com.springboot.todo.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        User user = new User(request.getName(), request.getUsername(), passwordEncoder.encode(request.getPassword()),
                Role.MEMBER);
        userRepository.save(user);
        return "Member registered successfully";
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(request.getUsername());
        return new AuthResponse(token, userDetails.getUsername());
    }

    public UserResponseDTO getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getName(), user.getRole().name());
    }
}
