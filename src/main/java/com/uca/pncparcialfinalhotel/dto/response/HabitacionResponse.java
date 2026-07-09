package com.uca.pncparcialfinalhotel.dto.response;

import com.uca.pncparcialfinalhotel.utils.enums.TipoHabitacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HabitacionResponse {
    private Long id;
    private Integer numero;
    private TipoHabitacion tipo;
    private BigDecimal precioPorNoche;
    private boolean disponible;
    private Long hotelId;
    private String hotelNombre;
}
