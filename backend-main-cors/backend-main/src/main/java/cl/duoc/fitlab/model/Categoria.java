package cl.duoc.fitlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// Misma estructura que Producto: _id interno de Mongo + id numérico para el frontend.
@Getter
@Setter
@Document(collection = "categorias")
public class Categoria {
    @Id
    @JsonIgnore
    private ObjectId _id;

    @Schema(description = "Identificador de la categoría", example = "1")
    @Field("id")
    private int id;

    @Schema(description = "Nombre de la categoría", example = "Proteínas & Aminoácidos")
    private String nombre;
}
