package com.uca.pncparcialfinalhotel.service;

import com.uca.pncparcialfinalhotel.dto.request.UsuarioCreateRequest;
import com.uca.pncparcialfinalhotel.dto.response.UsuarioResponse;
import com.uca.pncparcialfinalhotel.entity.Hotel;
import com.uca.pncparcialfinalhotel.entity.Usuario;
import com.uca.pncparcialfinalhotel.exception.BadRequestException;
import com.uca.pncparcialfinalhotel.exception.ResourceNotFoundException;
import com.uca.pncparcialfinalhotel.repository.HotelRepository;
import com.uca.pncparcialfinalhotel.repository.UsuarioRepository;
import com.uca.pncparcialfinalhotel.utils.enums.Rol;
import com.uca.pncparcialfinalhotel.utils.mappers.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final HotelRepository hotelRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponse crear(UsuarioCreateRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BadRequestException("Ya existe un usuario con el correo: " + request.getCorreo());
        }

        Hotel hotel = null;
        if (request.getRol() == Rol.RECEPCIONISTA) {
            if (request.getHotelId() == null) {
                throw new BadRequestException("Un RECEPCIONISTA debe tener un hotelId asignado");
            }
            hotel = hotelRepository.findById(request.getHotelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado: " + request.getHotelId()));
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .hotel(hotel)
                .tokenVersion(0)
                .build();

        return UsuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioMapper::toDTO).toList();
    }
}
