package com.uca.pncparcialfinalhotel.utils.mappers;

import com.uca.pncparcialfinalhotel.dto.response.ReservaResponse;
import com.uca.pncparcialfinalhotel.entity.Reserva;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class ReservaMapper {
    public static ReservaResponse toDTO(Reserva r) {
        long noches = ChronoUnit.DAYS.between(r.getFechaInicio(), r.getFechaFin());
        BigDecimal total = r.getHabitacion().getPrecioPorNoche().multiply(BigDecimal.valueOf(noches));

        return ReservaResponse.builder()
                .id(r.getId())
                .huespedId(r.getHuesped().getId())
                .huespedCorreo(r.getHuesped().getCorreo())
                .habitacionId(r.getHabitacion().getId())
                .habitacionNumero(r.getHabitacion().getNumero())
                .hotelId(r.getHotel().getId())
                .fechaInicio(r.getFechaInicio())
                .fechaFin(r.getFechaFin())
                .estado(r.getEstado())
                .fechaCreacion(r.getFechaCreacion())
                .totalEstimado(total)
                .build();
    }
}
