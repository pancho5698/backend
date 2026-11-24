package cl.duoc.fitlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// Lo que envía el carrito por cada producto: cuál y cuántos.
@Getter
@Setter
public class ItemCompra {
    @Schema(description = "Id del producto", example = "1")
    private int productoId;

    @Schema(description = "Unidades a comprar", example = "2")
    private int cantidad;
}
