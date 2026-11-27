package cl.duoc.fitlab.service;

import cl.duoc.fitlab.dto.LoginRequest;
import cl.duoc.fitlab.dto.RespuestaAuth;
import cl.duoc.fitlab.model.Usuario;
import cl.duoc.fitlab.repository.RepositorioUsuarios;
import cl.duoc.fitlab.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final RepositorioUsuarios repositorio;
    private final UsuariosService usuariosService;
    private final JwtService jwtService;

    public AuthService(RepositorioUsuarios repositorio, UsuariosService usuariosService, JwtService jwtService) {
        this.repositorio = repositorio;
        this.usuariosService = usuariosService;
        this.jwtService = jwtService;
    }

    public RespuestaAuth login(LoginRequest datos) {
        // Mismo mensaje si falla el correo o la clave: no damos pistas a un atacante.
        Usuario usuario = repositorio.findByCorreo(datos.getCorreo())
                .filter(u -> usuariosService.contrasenaCorrecta(u, datos.getContrasena()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos"));
        return new RespuestaAuth(jwtService.generarToken(usuario), usuario);
    }

    public RespuestaAuth registro(Usuario usuario) {
        // Quien se registra desde la tienda siempre es CLIENTE: nadie puede darse el rol ADMIN.
        usuario.setRol("CLIENTE");
        Usuario creado = usuariosService.crear(usuario);
        return new RespuestaAuth(jwtService.generarToken(creado), creado);
    }

    // Renovación: con un token todavía válido se entrega uno nuevo con duración completa.
    public RespuestaAuth renovar(String correo) {
        Usuario usuario = usuariosService.buscarPorCorreo(correo);
        return new RespuestaAuth(jwtService.generarToken(usuario), usuario);
    }
}
