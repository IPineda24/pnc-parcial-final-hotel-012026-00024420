package com.uca.pncparcialfinalhotel.controller;

import com.uca.pncparcialfinalhotel.dto.request.ChangePasswordRequest;
import com.uca.pncparcialfinalhotel.dto.request.LoginRequest;
import com.uca.pncparcialfinalhotel.dto.request.RefreshRequest;
import com.uca.pncparcialfinalhotel.dto.request.RegisterRequest;
import com.uca.pncparcialfinalhotel.dto.response.JwtResponse;
import com.uca.pncparcialfinalhotel.entity.Usuario;
import com.uca.pncparcialfinalhotel.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                                @AuthenticationPrincipal Usuario usuario) {
        authService.changePassword(usuario, request);
        return ResponseEntity.noContent().build();
    }
}
