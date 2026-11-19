package cl.duoc.fitlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// Lombok genera los getters y setters durante la compilación.
@Getter
@Setter
@Document(collection = "productos")
public class Producto {
    // _id es el identificador interno de Mongo; no lo enviamos al frontend.
    @Id
    @JsonIgnore
    private ObjectId _id;

    @Schema(description = "Identificador del producto", example = "1")
    @Field("id")
    private int id;

    @Schema(description = "Código único del producto", example = "PROT-001")
    private String codigo;

    @Schema(description = "Nombre que se muestra en la tienda", example = "Prostar 5 lb")
    private String nombre;

    @Schema(description = "Categoría del producto", example = "Proteínas & Aminoácidos")
    private String categoria;

    @Schema(description = "Precio normal en CLP", example = "90000")
    private int precio;

    @Schema(description = "Precio de oferta en CLP (opcional)", example = "79990")
    private Integer precioOferta;

    @Schema(description = "Unidades disponibles", example = "12")
    private int stock;

    @Schema(description = "Stock bajo el cual se avisa reposición", example = "5")
    private int stockCritico;

    @Schema(description = "Ruta de la imagen", example = "/img/proteina.jpg")
    private String imagen;

    @Schema(description = "Descripción del producto")
    private String descripcion;
}
