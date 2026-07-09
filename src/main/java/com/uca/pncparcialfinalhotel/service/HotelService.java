package com.uca.pncparcialfinalhotel.service;

import com.uca.pncparcialfinalhotel.dto.request.HotelRequest;
import com.uca.pncparcialfinalhotel.dto.response.HotelResponse;
import com.uca.pncparcialfinalhotel.entity.Hotel;
import com.uca.pncparcialfinalhotel.exception.ResourceNotFoundException;
import com.uca.pncparcialfinalhotel.repository.HotelRepository;
import com.uca.pncparcialfinalhotel.utils.mappers.HotelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelResponse crear(HotelRequest request) {
        Hotel hotel = Hotel.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .ciudad(request.getCiudad())
                .build();
        return HotelMapper.toDTO(hotelRepository.save(hotel));
    }

    public List<HotelResponse> listar() {
        return hotelRepository.findAll().stream().map(HotelMapper::toDTO).toList();
    }

    public HotelResponse buscarPorId(Long id) {
        return HotelMapper.toDTO(obtenerEntidad(id));
    }

    public Hotel obtenerEntidad(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado: " + id));
    }

    public void eliminar(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel no encontrado: " + id);
        }
        hotelRepository.deleteById(id);
    }
}
