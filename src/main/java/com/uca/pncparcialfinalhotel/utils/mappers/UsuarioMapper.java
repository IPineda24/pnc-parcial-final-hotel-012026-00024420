package com.uca.pncparcialfinalhotel.utils.mappers;

import com.uca.pncparcialfinalhotel.dto.response.UsuarioResponse;
import com.uca.pncparcialfinalhotel.entity.Usuario;

public class UsuarioMapper {
    public static UsuarioResponse toDTO(Usuario u) {
        return UsuarioResponse.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .correo(u.getCorreo())
                .rol(u.getRol().name())
                .hotelId(u.getHotel() != null ? u.getHotel().getId() : null)
                .build();
    }
}
