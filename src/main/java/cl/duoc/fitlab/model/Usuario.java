package cl.duoc.fitlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "usuarios")
public class Usuario {
    @Id
    @JsonIgnore
    private ObjectId _id;

    @Schema(description = "Identificador del usuario", example = "1")
    @Field("id")
    private int id;

    @Schema(description = "Nombre completo", example = "Ana Pérez")
    private String nombre;

    @Schema(description = "Correo, también se usa para iniciar sesión", example = "ana@gmail.com")
    private String correo;

    // WRITE_ONLY: se puede enviar al crear, pero nunca se devuelve en las respuestas.
    @Schema(description = "Contraseña (mínimo 6 caracteres). Se guarda encriptada.", example = "clave123")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;

    @Schema(description = "Rol del usuario", example = "CLIENTE", allowableValues = {"ADMIN", "VENDEDOR", "CLIENTE"})
    private String rol;
}
