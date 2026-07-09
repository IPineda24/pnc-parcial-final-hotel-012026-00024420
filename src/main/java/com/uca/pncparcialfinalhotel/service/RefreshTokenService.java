package com.uca.pncparcialfinalhotel.service;

import com.uca.pncparcialfinalhotel.entity.RefreshToken;
import com.uca.pncparcialfinalhotel.entity.Usuario;
import com.uca.pncparcialfinalhotel.exception.InvalidTokenException;
import com.uca.pncparcialfinalhotel.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    public RefreshToken crear(Usuario usuario) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .usuario(usuario)
                .fechaExpiracion(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000))
                .revocado(false)
                .tokenVersion(usuario.getTokenVersion())
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Valida que el refresh token exista, no este revocado, no haya expirado, y que su
     * tokenVersion coincida con la version ACTUAL del usuario (si el usuario cambio su
     * password despues de emitir este refresh token, queda invalido de inmediato).
     */
    public RefreshToken validar(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token invalido"));

        if (refreshToken.isRevocado()) {
            throw new InvalidTokenException("Refresh token revocado");
        }
        if (refreshToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Refresh token expirado");
        }
        if (!refreshToken.getTokenVersion().equals(refreshToken.getUsuario().getTokenVersion())) {
            throw new InvalidTokenException("Refresh token invalidado por cambio de password");
        }
        return refreshToken;
    }

    public void revocar(RefreshToken refreshToken) {
        refreshToken.setRevocado(true);
        refreshTokenRepository.save(refreshToken);
    }

    /**
     * Revoca TODOS los refresh tokens activos de un usuario. Se invoca al cambiar el password
     * (regla de negocio Opcion A), ademas del chequeo de tokenVersion.
     */
    public void revocarTodosDelUsuario(Long usuarioId) {
        refreshTokenRepository.findByUsuarioIdAndRevocadoFalse(usuarioId)
                .forEach(rt -> {
                    rt.setRevocado(true);
                    refreshTokenRepository.save(rt);
                });
    }
}
