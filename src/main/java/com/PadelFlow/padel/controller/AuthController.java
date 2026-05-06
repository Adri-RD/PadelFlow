package com.PadelFlow.padel.controller;

import com.PadelFlow.padel.dto.*;
import com.PadelFlow.padel.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            return ResponseEntity.ok(authService.register(req));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("mensaje", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            return ResponseEntity.ok(authService.login(req));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(java.util.Map.of("mensaje", e.getMessage()));
        }
    }
}