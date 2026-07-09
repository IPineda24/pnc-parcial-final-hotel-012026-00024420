package com.uca.pncparcialfinalhotel.dto.request;

import com.uca.pncparcialfinalhotel.utils.enums.TipoHabitacion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HabitacionRequest {
    @NotNull(message = "El numero de habitacion es obligatorio")
    private Integer numero;

    @NotNull(message = "El tipo de habitacion es obligatorio")
    private TipoHabitacion tipo;

    @NotNull(message = "El precio por noche es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precioPorNoche;

    private Boolean disponible;

    // Solo lo usa el ADMIN al crear una habitacion; el RECEPCIONISTA siempre opera sobre su propio hotel
    private Long hotelId;
}
