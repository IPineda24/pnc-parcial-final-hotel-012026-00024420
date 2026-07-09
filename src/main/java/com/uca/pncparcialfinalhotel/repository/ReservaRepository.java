package com.uca.pncparcialfinalhotel.repository;

import com.uca.pncparcialfinalhotel.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByHuespedId(Long huespedId);
    List<Reserva> findByHotelId(Long hotelId);
    Optional<Reserva> findByIdAndHuespedId(Long id, Long huespedId);
    Optional<Reserva> findByIdAndHotelId(Long id, Long hotelId);

    // Reservas activas (no canceladas) de una habitacion que se solapan con el rango solicitado
    @Query("""
            SELECT r FROM Reserva r
            WHERE r.habitacion.id = :habitacionId
            AND r.estado <> com.uca.pncparcialfinalhotel.utils.enums.EstadoReserva.CANCELADA
            AND r.fechaInicio < :fechaFin
            AND r.fechaFin > :fechaInicio
            """)
    List<Reserva> findSolapadas(@Param("habitacionId") Long habitacionId,
                                 @Param("fechaInicio") LocalDate fechaInicio,
                                 @Param("fechaFin") LocalDate fechaFin);
}
