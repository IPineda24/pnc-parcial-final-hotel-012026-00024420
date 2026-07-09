package com.uca.pncparcialfinalhotel.controller;

import com.uca.pncparcialfinalhotel.dto.request.ReservaCreateRequest;
import com.uca.pncparcialfinalhotel.dto.request.ReservaEstadoRequest;
import com.uca.pncparcialfinalhotel.dto.response.ReservaResponse;
import com.uca.pncparcialfinalhotel.entity.Usuario;
import com.uca.pncparcialfinalhotel.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    // HUESPED: crear su propia reserva
    @PostMapping
    @PreAuthorize("hasRole('HUESPED')")
    public ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaCreateRequest request,
                                                   @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(201).body(reservaService.crear(request, usuario));
    }

    // HUESPED: ver solo sus propias reservas
    @GetMapping("/mias")
    @PreAuthorize("hasRole('HUESPED')")
    public ResponseEntity<List<ReservaResponse>> misReservas(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(reservaService.misReservas(usuario));
    }

    // HUESPED: cancelar su propia reserva
    @PostMapping("/{id}/cancelar-mia")
    @PreAuthorize("hasRole('HUESPED')")
    public ResponseEntity<ReservaResponse> cancelarPropia(@PathVariable Long id,
                                                            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(reservaService.cancelarPropia(id, usuario));
    }

    // RECEPCIONISTA (solo su hotel) y ADMIN (todos): ver reservas
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<ReservaResponse>> listar(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(reservaService.listar(usuario));
    }

    // RECEPCIONISTA (solo su hotel) y ADMIN: confirmar/cancelar/completar una reserva
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ReservaResponse> cambiarEstado(@PathVariable Long id,
                                                           @Valid @RequestBody ReservaEstadoRequest request,
                                                           @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(reservaService.cambiarEstado(id, request.getEstado(), usuario));
    }
}
