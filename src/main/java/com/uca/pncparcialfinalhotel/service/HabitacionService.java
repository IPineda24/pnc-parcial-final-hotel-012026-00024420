package com.uca.pncparcialfinalhotel.service;

import com.uca.pncparcialfinalhotel.dto.request.HabitacionRequest;
import com.uca.pncparcialfinalhotel.dto.response.HabitacionResponse;
import com.uca.pncparcialfinalhotel.entity.Habitacion;
import com.uca.pncparcialfinalhotel.entity.Hotel;
import com.uca.pncparcialfinalhotel.entity.Usuario;
import com.uca.pncparcialfinalhotel.exception.BadRequestException;
import com.uca.pncparcialfinalhotel.exception.ResourceNotFoundException;
import com.uca.pncparcialfinalhotel.repository.HabitacionRepository;
import com.uca.pncparcialfinalhotel.utils.enums.Rol;
import com.uca.pncparcialfinalhotel.utils.mappers.HabitacionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HotelService hotelService;

    public HabitacionResponse crear(HabitacionRequest request, Usuario solicitante) {
        Long hotelId = resolverHotelId(request.getHotelId(), solicitante);
        Hotel hotel = hotelService.obtenerEntidad(hotelId);

        Habitacion habitacion = Habitacion.builder()
                .numero(request.getNumero())
                .tipo(request.getTipo())
                .precioPorNoche(request.getPrecioPorNoche())
                .disponible(request.getDisponible() == null || request.getDisponible())
                .hotel(hotel)
                .build();

        return HabitacionMapper.toDTO(habitacionRepository.save(habitacion));
    }

    public List<HabitacionResponse> listar(Usuario solicitante) {
        List<Habitacion> habitaciones = (solicitante.getRol() == Rol.RECEPCIONISTA)
                ? habitacionRepository.findByHotelId(solicitante.getHotel().getId())
                : habitacionRepository.findAll();

        return habitaciones.stream().map(HabitacionMapper::toDTO).toList();
    }

    public HabitacionResponse actualizar(Long id, HabitacionRequest request, Usuario solicitante) {
        Habitacion habitacion = obtenerEntidad(id);
        verificarPerteneceASuHotel(habitacion, solicitante);

        habitacion.setNumero(request.getNumero());
        habitacion.setTipo(request.getTipo());
        habitacion.setPrecioPorNoche(request.getPrecioPorNoche());
        if (request.getDisponible() != null) {
            habitacion.setDisponible(request.getDisponible());
        }

        return HabitacionMapper.toDTO(habitacionRepository.save(habitacion));
    }

    public void eliminar(Long id, Usuario solicitante) {
        Habitacion habitacion = obtenerEntidad(id);
        verificarPerteneceASuHotel(habitacion, solicitante);
        habitacionRepository.delete(habitacion);
    }

    public Habitacion obtenerEntidad(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion no encontrada: " + id));
    }

    // *** Autorizacion por atributo (requisito base 2.2) ***
    // Un RECEPCIONISTA solo puede operar sobre habitaciones de SU PROPIO hotel.
    // Esto no se resuelve verificando el rol: hay que comparar el hotel del usuario
    // contra el hotel de la habitacion.
    private void verificarPerteneceASuHotel(Habitacion habitacion, Usuario solicitante) {
        if (solicitante.getRol() == Rol.RECEPCIONISTA
                && !habitacion.getHotel().getId().equals(solicitante.getHotel().getId())) {
            throw new AccessDeniedException("Esta habitacion no pertenece a tu hotel");
        }
    }

    private Long resolverHotelId(Long hotelIdDelRequest, Usuario solicitante) {
        if (solicitante.getRol() == Rol.RECEPCIONISTA) {
            return solicitante.getHotel().getId(); // ignora lo que venga en el body, siempre su propio hotel
        }
        if (hotelIdDelRequest == null) {
            throw new BadRequestException("Debes indicar el hotelId");
        }
        return hotelIdDelRequest;
    }
}
