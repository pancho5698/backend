package cl.duoc.fitlab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cl.duoc.fitlab.dto.LoginRequest;
import cl.duoc.fitlab.dto.RespuestaAuth;
import cl.duoc.fitlab.model.Usuario;
import cl.duoc.fitlab.repository.RepositorioUsuarios;
import cl.duoc.fitlab.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Pruebas del login, del registro y del token JWT.
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private RepositorioUsuarios repositorio;
    @Mock
    private UsuariosService usuariosService;

    // El JwtService es real: así probamos que el token de verdad lleva el correo y el rol.
    private final JwtService jwtService = new JwtService("clave-de-prueba-para-los-tests-de-fitlab-2026", 60);
    private AuthService servicio;

    @BeforeEach
    void preparar() {
        servicio = new AuthService(repositorio, usuariosService, jwtService);
    }

    private Usuario admin() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Administrador FitLab");
        usuario.setCorreo("admin@fitlab.cl");
        usuario.setRol("ADMIN");
        return usuario;
    }

    private LoginRequest login(String correo, String contrasena) {
        LoginRequest datos = new LoginRequest();
        datos.setCorreo(correo);
        datos.setContrasena(contrasena);
        return datos;
    }

    @Test
    void loginConClaveIncorrectaRespondeError401() {
        Usuario admin = admin();
        when(repositorio.findByCorreo("admin@fitlab.cl")).thenReturn(Optional.of(admin));
        when(usuariosService.contrasenaCorrecta(admin, "mala")).thenReturn(false);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> servicio.login(login("admin@fitlab.cl", "mala")));

        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatusCode());
    }

    @Test
    void loginCorrectoEntregaUnTokenConElCorreoYElRol() {
        Usuario admin = admin();
        when(repositorio.findByCorreo("admin@fitlab.cl")).thenReturn(Optional.of(admin));
        when(usuariosService.contrasenaCorrecta(admin, "admin123")).thenReturn(true);

        RespuestaAuth respuesta = servicio.login(login("admin@fitlab.cl", "admin123"));
        Claims datos = jwtService.validarToken(respuesta.getToken());

        assertEquals("admin@fitlab.cl", datos.getSubject());
        assertEquals("ADMIN", datos.get("rol", String.class));
    }

    @Test
    void registroSiempreCreaUnClienteAunquePidaSerAdmin() {
        when(usuariosService.crear(any(Usuario.class))).thenAnswer(invocacion -> invocacion.getArgument(0));
        Usuario nuevo = new Usuario();
        nuevo.setCorreo("ana@gmail.com");
        nuevo.setRol("ADMIN");

        RespuestaAuth respuesta = servicio.registro(nuevo);

        assertEquals("CLIENTE", respuesta.getUsuario().getRol());
    }

    @Test
    void unTokenAlteradoEsRechazado() {
        String token = jwtService.generarToken(admin());

        // Cambiar un solo carácter rompe la firma: el token deja de ser válido.
        assertThrows(JwtException.class, () -> jwtService.validarToken(token + "x"));
    }
}
