package com.uca.pncparcialfinalhotel.service;

import com.uca.pncparcialfinalhotel.dto.request.ReservaCreateRequest;
import com.uca.pncparcialfinalhotel.dto.response.ReservaResponse;
import com.uca.pncparcialfinalhotel.entity.Habitacion;
import com.uca.pncparcialfinalhotel.entity.Reserva;
import com.uca.pncparcialfinalhotel.entity.Usuario;
import com.uca.pncparcialfinalhotel.exception.BadRequestException;
import com.uca.pncparcialfinalhotel.exception.ResourceNotFoundException;
import com.uca.pncparcialfinalhotel.repository.ReservaRepository;
import com.uca.pncparcialfinalhotel.utils.enums.EstadoReserva;
import com.uca.pncparcialfinalhotel.utils.enums.Rol;
import com.uca.pncparcialfinalhotel.utils.mappers.ReservaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final HabitacionService habitacionService;

    public ReservaResponse crear(ReservaCreateRequest request, Usuario huesped) {
        if (!request.getFechaFin().isAfter(request.getFechaInicio())) {
            throw new BadRequestException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        Habitacion habitacion = habitacionService.obtenerEntidad(request.getHabitacionId());

        if (!habitacion.isDisponible()) {
            throw new BadRequestException("La habitacion no esta disponible");
        }

        boolean haySolapamiento = !reservaRepository.findSolapadas(
                habitacion.getId(), request.getFechaInicio(), request.getFechaFin()).isEmpty();
        if (haySolapamiento) {
            throw new BadRequestException("La habitacion ya esta reservada en ese rango de fechas");
        }

        Reserva reserva = Reserva.builder()
                .huesped(huesped)
                .habitacion(habitacion)
                .hotel(habitacion.getHotel())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .estado(EstadoReserva.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();

        return ReservaMapper.toDTO(reservaRepository.save(reserva));
    }

    public List<ReservaResponse> misReservas(Usuario huesped) {
        return reservaRepository.findByHuespedId(huesped.getId()).stream()
                .map(ReservaMapper::toDTO)
                .toList();
    }

    // RECEPCIONISTA (solo su hotel) y ADMIN (todos)
    public List<ReservaResponse> listar(Usuario solicitante) {
        List<Reserva> reservas = (solicitante.getRol() == Rol.RECEPCIONISTA)
                ? reservaRepository.findByHotelId(solicitante.getHotel().getId())
                : reservaRepository.findAll();

        return reservas.stream().map(ReservaMapper::toDTO).toList();
    }

    public ReservaResponse cancelarPropia(Long id, Usuario huesped) {
        Reserva reserva = reservaRepository.findByIdAndHuespedId(id, huesped.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No tienes una reserva con ese ID"));

        reserva.setEstado(EstadoReserva.CANCELADA);
        return ReservaMapper.toDTO(reservaRepository.save(reserva));
    }

    // *** Autorizacion por atributo (requisito base 2.2) ***
    // El RECEPCIONISTA solo puede confirmar/cancelar reservas de SU PROPIO hotel.
    public ReservaResponse cambiarEstado(Long id, EstadoReserva nuevoEstado, Usuario solicitante) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada: " + id));

        if (solicitante.getRol() == Rol.RECEPCIONISTA
                && !reserva.getHotel().getId().equals(solicitante.getHotel().getId())) {
            throw new AccessDeniedException("Esta reserva no pertenece a tu hotel");
        }

        reserva.setEstado(nuevoEstado);
        return ReservaMapper.toDTO(reservaRepository.save(reserva));
    }
}
