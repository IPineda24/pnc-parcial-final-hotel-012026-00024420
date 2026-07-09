package com.uca.pncparcialfinalhotel.dto.request;

import com.uca.pncparcialfinalhotel.utils.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioCreateRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    // Obligatorio solo si rol == RECEPCIONISTA
    private Long hotelId;
}
