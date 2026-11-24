package cl.duoc.fitlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// Datos que envía el formulario de login.
@Getter
@Setter
public class LoginRequest {
    @Schema(description = "Correo del usuario", example = "admin@fitlab.cl")
    private String correo;

    @Schema(description = "Contraseña", example = "admin123")
    private String contrasena;
}
