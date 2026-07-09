package com.uca.pncparcialfinalhotel.bootstrap;

import com.uca.pncparcialfinalhotel.entity.Habitacion;
import com.uca.pncparcialfinalhotel.entity.Hotel;
import com.uca.pncparcialfinalhotel.entity.Usuario;
import com.uca.pncparcialfinalhotel.repository.HabitacionRepository;
import com.uca.pncparcialfinalhotel.repository.HotelRepository;
import com.uca.pncparcialfinalhotel.repository.UsuarioRepository;
import com.uca.pncparcialfinalhotel.utils.enums.Rol;
import com.uca.pncparcialfinalhotel.utils.enums.TipoHabitacion;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Crea datos de prueba al arrancar: 2 hoteles, un ADMIN, un RECEPCIONISTA (asignado
 * al primer hotel), un HUESPED, y un par de habitaciones. Asi se puede probar login
 * y roles sin tener que insertar nada a mano. Ver credenciales en el README.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final HabitacionRepository habitacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (hotelRepository.count() > 0) {
            return; // ya sembrado, evita duplicados en reinicios
        }

        Hotel hotelCentro = hotelRepository.save(Hotel.builder()
                .nombre("Hotel Centro")
                .direccion("Av. Principal 123")
                .ciudad("San Salvador")
                .build());

        Hotel hotelPlaya = hotelRepository.save(Hotel.builder()
                .nombre("Hotel Playa")
                .direccion("Km 45 Carretera Litoral")
                .ciudad("La Libertad")
                .build());

        habitacionRepository.save(Habitacion.builder()
                .numero(101).tipo(TipoHabitacion.SENCILLA)
                .precioPorNoche(new BigDecimal("45.00")).disponible(true)
                .hotel(hotelCentro).build());

        habitacionRepository.save(Habitacion.builder()
                .numero(102).tipo(TipoHabitacion.DOBLE)
                .precioPorNoche(new BigDecimal("70.00")).disponible(true)
                .hotel(hotelCentro).build());

        habitacionRepository.save(Habitacion.builder()
                .numero(201).tipo(TipoHabitacion.SUITE)
                .precioPorNoche(new BigDecimal("150.00")).disponible(true)
                .hotel(hotelPlaya).build());

        usuarioRepository.save(Usuario.builder()
                .nombre("Admin General")
                .correo("admin@hotel.com")
                .password(passwordEncoder.encode("Admin123!"))
                .rol(Rol.ADMIN)
                .tokenVersion(0)
                .build());

        usuarioRepository.save(Usuario.builder()
                .nombre("Recepcionista Centro")
                .correo("recepcion@hotel.com")
                .password(passwordEncoder.encode("Recepcion123!"))
                .rol(Rol.RECEPCIONISTA)
                .hotel(hotelCentro)
                .tokenVersion(0)
                .build());

        usuarioRepository.save(Usuario.builder()
                .nombre("Huesped Demo")
                .correo("huesped@demo.com")
                .password(passwordEncoder.encode("Huesped123!"))
                .rol(Rol.HUESPED)
                .tokenVersion(0)
                .build());
    }
}
