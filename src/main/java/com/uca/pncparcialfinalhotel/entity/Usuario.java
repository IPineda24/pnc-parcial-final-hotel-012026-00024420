package com.uca.pncparcialfinalhotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uca.pncparcialfinalhotel.utils.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // Solo aplica para RECEPCIONISTA: a que sucursal (hotel) pertenece.
    // ADMIN y HUESPED lo dejan en null.
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    // --- Regla de negocio (Opcion A): invalidacion de tokens por cambio de password ---
    // Se incrusta como claim en cada JWT emitido. Si el usuario cambia su password,
    // este numero se incrementa y CUALQUIER token viejo (con version anterior) deja
    // de ser valido de inmediato, sin importar si ya expiro o no.
    @Builder.Default
    @Column(nullable = false)
    private Integer tokenVersion = 0;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
