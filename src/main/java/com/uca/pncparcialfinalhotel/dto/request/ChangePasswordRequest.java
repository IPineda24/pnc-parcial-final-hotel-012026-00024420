package com.uca.pncparcialfinalhotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "El password actual es obligatorio")
    private String passwordActual;

    @NotBlank(message = "El nuevo password es obligatorio")
    @Size(min = 8, message = "El nuevo password debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "El nuevo password debe incluir mayuscula, minuscula y numero")
    private String passwordNuevo;
}
