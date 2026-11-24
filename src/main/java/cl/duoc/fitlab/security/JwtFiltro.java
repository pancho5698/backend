package cl.duoc.fitlab.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// Se ejecuta en CADA petición: lee el token de la cabecera Authorization y,
// si es válido, le dice a Spring quién es el usuario y qué rol tiene.
@Component
public class JwtFiltro extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtFiltro(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest peticion, HttpServletResponse respuesta, FilterChain cadena)
            throws ServletException, IOException {
        String cabecera = peticion.getHeader("Authorization");
        if (cabecera != null && cabecera.startsWith("Bearer ")) {
            try {
                Claims datos = jwtService.validarToken(cabecera.substring(7));
                String rol = datos.get("rol", String.class);
                // Spring usa el prefijo ROLE_ para los roles: ROLE_ADMIN, ROLE_VENDEDOR, ROLE_CLIENTE.
                var usuario = new UsernamePasswordAuthenticationToken(
                        datos.getSubject(), null, List.of(new SimpleGrantedAuthority("ROLE_" + rol)));
                SecurityContextHolder.getContext().setAuthentication(usuario);
            } catch (JwtException | IllegalArgumentException e) {
                // Token alterado o vencido: la petición sigue sin usuario y Spring responde 401 si la ruta lo exige.
            }
        }
        cadena.doFilter(peticion, respuesta);
    }
}
