package com.dreadbots.projectmanagement.controller;

import com.dreadbots.projectmanagement.dto.AuthResponse;
import com.dreadbots.projectmanagement.dto.LoginRequest;
import com.dreadbots.projectmanagement.entity.UserEntity;
import com.dreadbots.projectmanagement.security.JwtService;
import com.dreadbots.projectmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = userService.findByUsername(request.getUsername());
        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(new AuthResponse(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getRole().name(),
            token
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.findByUsername(username));
    }
}
