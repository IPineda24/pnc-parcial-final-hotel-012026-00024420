package com.uca.pncparcialfinalhotel.dto.response;

import com.uca.pncparcialfinalhotel.utils.enums.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservaResponse {
    private Long id;
    private Long huespedId;
    private String huespedCorreo;
    private Long habitacionId;
    private Integer habitacionNumero;
    private Long hotelId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoReserva estado;
    private LocalDateTime fechaCreacion;
    private BigDecimal totalEstimado;
}
