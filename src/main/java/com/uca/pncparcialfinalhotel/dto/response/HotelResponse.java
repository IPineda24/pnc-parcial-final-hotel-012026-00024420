package com.uca.pncparcialfinalhotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
    private Long id;
    private String nombre;
    private String direccion;
    private String ciudad;
}
