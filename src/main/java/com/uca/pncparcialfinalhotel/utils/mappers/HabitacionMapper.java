package com.uca.pncparcialfinalhotel.utils.mappers;

import com.uca.pncparcialfinalhotel.dto.response.HabitacionResponse;
import com.uca.pncparcialfinalhotel.entity.Habitacion;

public class HabitacionMapper {
    public static HabitacionResponse toDTO(Habitacion h) {
        return HabitacionResponse.builder()
                .id(h.getId())
                .numero(h.getNumero())
                .tipo(h.getTipo())
                .precioPorNoche(h.getPrecioPorNoche())
                .disponible(h.isDisponible())
                .hotelId(h.getHotel().getId())
                .hotelNombre(h.getHotel().getNombre())
                .build();
    }
}
