package cl.duoc.fitlab.controller;

import cl.duoc.fitlab.dto.LoginRequest;
import cl.duoc.fitlab.dto.RespuestaAuth;
import cl.duoc.fitlab.model.Usuario;
import cl.duoc.fitlab.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Login, registro y renovación del token JWT")
public class AuthController {
    private final AuthService servicio;

    public AuthController(AuthService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Iniciar sesión", description = "Devuelve el token JWT y los datos del usuario.")
    @ApiResponse(responseCode = "200", description = "Login correcto.")
    @ApiResponse(responseCode = "401", description = "Correo o contraseña incorrectos.", content = @Content)
    @SecurityRequirements // Ruta pública: no necesita token.
    @PostMapping("/login")
    public RespuestaAuth login(@RequestBody LoginRequest datos) {
        return servicio.login(datos);
    }

    @Operation(summary = "Registrarse", description = "Crea una cuenta con rol CLIENTE y devuelve su token.")
    @ApiResponse(responseCode = "201", description = "Cuenta creada.")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o correo repetido.", content = @Content)
    @SecurityRequirements
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public RespuestaAuth registro(@RequestBody Usuario usuario) {
        return servicio.registro(usuario);
    }

    @Operation(summary = "Renovar el token", description = "Con un token válido entrega uno nuevo, con duración completa.")
    @ApiResponse(responseCode = "200", description = "Token renovado.")
    @ApiResponse(responseCode = "401", description = "Sin token o token vencido.", content = @Content)
    @PostMapping("/refresh")
    public RespuestaAuth renovar(Authentication autenticacion) {
        // autenticacion.getName() es el correo que venía dentro del token.
        return servicio.renovar(autenticacion.getName());
    }
}
