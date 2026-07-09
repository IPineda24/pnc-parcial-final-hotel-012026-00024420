package com.uca.pncparcialfinalhotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HotelRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String direccion;
    private String ciudad;
}
