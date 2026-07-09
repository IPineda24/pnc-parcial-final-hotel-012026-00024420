package com.uca.pncparcialfinalhotel.security;

import com.uca.pncparcialfinalhotel.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null) {
            try {
                Claims claims = jwtUtil.parseClaims(token); // valida firma + expiracion
                String correo = claims.getSubject();
                Integer tokenVersionDelClaim = claims.get("tokenVersion", Integer.class);

                Usuario usuario = (Usuario) userDetailsService.loadUserByUsername(correo);

                // *** Regla de negocio (Opcion A) ***
                // Aunque el token sea valido (firma correcta y no expirado), si el usuario
                // cambio su password despues de que este token fue emitido, su tokenVersion
                // actual sera mayor al que quedo "congelado" en el claim -> se rechaza
                // inmediatamente, sin esperar a que expire.
                if (tokenVersionDelClaim != null && tokenVersionDelClaim.equals(usuario.getTokenVersion())) {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario, null, usuario.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // si no coincide, no se autentica -> el JwtAuthEntryPoint respondera 401
            } catch (JwtException | IllegalArgumentException e) {
                // token invalido/expirado/manipulado: no autenticamos, seguimos la cadena
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
