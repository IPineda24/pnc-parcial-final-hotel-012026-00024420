package com.uca.pncparcialfinalhotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotBlank(message = "El refreshToken es obligatorio")
    private String refreshToken;
}
