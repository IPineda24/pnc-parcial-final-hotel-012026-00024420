package com.uca.pncparcialfinalhotel.entity;

import com.uca.pncparcialfinalhotel.utils.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "huesped_id")
    private Usuario huesped;

    @ManyToOne(optional = false)
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;

    // Denormalizado para poder filtrar reservas por sucursal sin hacer join
    // hasta Habitacion en cada consulta (autorizacion por atributo del Recepcionista).
    @ManyToOne(optional = false)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}
