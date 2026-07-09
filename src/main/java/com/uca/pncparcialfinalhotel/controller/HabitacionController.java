package com.uca.pncparcialfinalhotel.controller;

import com.uca.pncparcialfinalhotel.dto.request.HabitacionRequest;
import com.uca.pncparcialfinalhotel.dto.response.HabitacionResponse;
import com.uca.pncparcialfinalhotel.entity.Usuario;
import com.uca.pncparcialfinalhotel.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    // Ver habitaciones disponibles es necesario para que un HUESPED pueda reservar
    @GetMapping
    public ResponseEntity<List<HabitacionResponse>> listar(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(habitacionService.listar(usuario));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<HabitacionResponse> crear(@Valid @RequestBody HabitacionRequest request,
                                                      @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(201).body(habitacionService.crear(request, usuario));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<HabitacionResponse> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody HabitacionRequest request,
                                                           @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(habitacionService.actualizar(id, request, usuario));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        habitacionService.eliminar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
