package com.uca.pncparcialfinalhotel.service;

import com.uca.pncparcialfinalhotel.dto.request.ChangePasswordRequest;
import com.uca.pncparcialfinalhotel.dto.request.LoginRequest;
import com.uca.pncparcialfinalhotel.dto.request.RegisterRequest;
import com.uca.pncparcialfinalhotel.dto.response.JwtResponse;
import com.uca.pncparcialfinalhotel.entity.RefreshToken;
import com.uca.pncparcialfinalhotel.entity.Usuario;
import com.uca.pncparcialfinalhotel.exception.BadRequestException;
import com.uca.pncparcialfinalhotel.repository.UsuarioRepository;
import com.uca.pncparcialfinalhotel.security.JwtUtil;
import com.uca.pncparcialfinalhotel.utils.enums.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    // Auto-registro publico: siempre crea un HUESPED (crear ADMIN/RECEPCIONISTA es tarea del ADMIN)
    public void register(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BadRequestException("Ya existe un usuario con el correo: " + request.getCorreo());
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Rol.HUESPED)
                .tokenVersion(0)
                .build();

        usuarioRepository.save(usuario);
    }

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        return emitirTokens(usuario);
    }

    public JwtResponse refresh(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenService.validar(refreshTokenStr);
        Usuario usuario = refreshToken.getUsuario();

        // Rotacion: el refresh token usado se revoca y se emite uno nuevo
        refreshTokenService.revocar(refreshToken);
        return emitirTokens(usuario);
    }

    /**
     * Cambia el password del usuario autenticado e implementa la regla de negocio
     * (Opcion A): incrementa tokenVersion (invalida cualquier access token viejo en el
     * siguiente request, sin esperar su expiracion) y revoca todos los refresh tokens
     * activos (para que tampoco se pueda renovar la sesion con un token emitido antes).
     */
    public void changePassword(Usuario usuario, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            throw new BadCredentialsException("El password actual no es correcto");
        }

        usuario.setPassword(passwordEncoder.encode(request.getPasswordNuevo()));
        usuario.setTokenVersion(usuario.getTokenVersion() + 1);
        usuarioRepository.save(usuario);

        refreshTokenService.revocarTodosDelUsuario(usuario.getId());
    }

    private JwtResponse emitirTokens(Usuario usuario) {
        String rol = "ROLE_" + usuario.getRol().name();
        String accessToken = jwtUtil.generateAccessToken(usuario.getCorreo(), rol, usuario.getTokenVersion());
        RefreshToken refreshToken = refreshTokenService.crear(usuario);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresInSeconds(jwtUtil.getAccessExpirationMs() / 1000)
                .correo(usuario.getCorreo())
                .rol(rol)
                .build();
    }
}
