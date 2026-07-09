package com.uca.pncparcialfinalhotel.controller;

import com.uca.pncparcialfinalhotel.dto.request.HotelRequest;
import com.uca.pncparcialfinalhotel.dto.response.HotelResponse;
import com.uca.pncparcialfinalhotel.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoteles")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelResponse>> listar() {
        return ResponseEntity.ok(hotelService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // aqui tuve que corregir a la IA
    public ResponseEntity<HotelResponse> crear(@Valid @RequestBody HotelRequest request) {
        return ResponseEntity.status(201).body(hotelService.crear(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        hotelService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
