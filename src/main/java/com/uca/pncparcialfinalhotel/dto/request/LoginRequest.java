package com.uca.pncparcialfinalhotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "El password es obligatorio")
    private String password;
}
