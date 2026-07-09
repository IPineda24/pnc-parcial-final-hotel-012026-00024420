package com.uca.pncparcialfinalhotel.entity;

import com.uca.pncparcialfinalhotel.utils.enums.TipoHabitacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "habitaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoHabitacion tipo;

    @Column(nullable = false)
    private BigDecimal precioPorNoche;

    // Disponibilidad "general" del cuarto (ej. fuera de servicio por mantenimiento).
    // La disponibilidad por fechas concretas se calcula contra las reservas activas.
    @Builder.Default
    private boolean disponible = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
