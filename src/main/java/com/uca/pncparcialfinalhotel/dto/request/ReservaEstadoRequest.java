package com.uca.pncparcialfinalhotel.dto.request;

import com.uca.pncparcialfinalhotel.utils.enums.EstadoReserva;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaEstadoRequest {
    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoReserva estado;
}
