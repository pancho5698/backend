package cl.duoc.fitlab.dto;

import cl.duoc.fitlab.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

// Lo que recibe el front al iniciar sesión: el token y los datos del usuario (sin contraseña).
@Getter
@AllArgsConstructor
public class RespuestaAuth {
    @Schema(description = "Token JWT. Se envía en la cabecera Authorization: Bearer <token>")
    private String token;

    @Schema(description = "Usuario que inició sesión")
    private Usuario usuario;
}
