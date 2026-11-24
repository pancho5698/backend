package cl.duoc.fitlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "boletas")
public class Boleta {
    @Id
    @JsonIgnore
    private ObjectId _id;

    @Schema(description = "Número de boleta", example = "1")
    @Field("id")
    private int id;

    @Schema(description = "Fecha y hora de la compra")
    private LocalDateTime fecha;

    @Schema(description = "Correo del cliente que compró", example = "ana@gmail.com")
    private String correoCliente;

    // DETALLE_BOLETA del modelo: en Mongo va embebido dentro de la boleta.
    @Schema(description = "Productos comprados")
    private List<DetalleBoleta> detalles = new ArrayList<>();

    @Schema(description = "Suma de los subtotales en CLP", example = "159980")
    private int total;
}
