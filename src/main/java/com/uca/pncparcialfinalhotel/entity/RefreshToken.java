package com.uca.pncparcialfinalhotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Token opaco (UUID), no un JWT: asi lo podemos revocar/consultar en la BD sin decodificar nada
    @Column(nullable = false, unique = true, length = 100)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Builder.Default
    @Column(nullable = false)
    private boolean revocado = false;

    // Snapshot del tokenVersion del usuario al emitir este refresh token.
    // Si el usuario cambia su password (tokenVersion++), este refresh token queda
    // invalido aunque no haya expirado ni haya sido revocado explicitamente.
    @Column(nullable = false)
    private Integer tokenVersion;
}
