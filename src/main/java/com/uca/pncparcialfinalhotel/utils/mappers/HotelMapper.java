package com.uca.pncparcialfinalhotel.utils.mappers;

import com.uca.pncparcialfinalhotel.dto.response.HotelResponse;
import com.uca.pncparcialfinalhotel.entity.Hotel;

public class HotelMapper {
    public static HotelResponse toDTO(Hotel h) {
        return HotelResponse.builder()
                .id(h.getId())
                .nombre(h.getNombre())
                .direccion(h.getDireccion())
                .ciudad(h.getCiudad())
                .build();
    }
}
