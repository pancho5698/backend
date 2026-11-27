package cl.duoc.fitlab.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// No es una colección: cada detalle se guarda DENTRO de su boleta (documento embebido de Mongo).
@Getter
@Setter
public class DetalleBoleta {
    @Schema(description = "Id del producto comprado", example = "1")
    private int productoId;

    @Schema(description = "Nombre del producto al momento de la compra", example = "Prostar 5 lb")
    private String nombre;

    @Schema(description = "Precio unitario cobrado (usa la oferta si existe)", example = "79990")
    private int precioUnitario;

    @Schema(description = "Unidades compradas", example = "2")
    private int cantidad;

    @Schema(description = "precioUnitario x cantidad", example = "159980")
    private int subtotal;
}
