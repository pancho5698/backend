package cl.duoc.fitlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

// Datos que llegan para crear una boleta. El precio y el total NO vienen del front:
// los calcula el backend con los datos de la base, así nadie puede cambiarlos.
@Getter
@Setter
public class NuevaBoleta {
    // El cliente NO viene aquí: se toma del token JWT del usuario logueado.
    @Schema(description = "Productos del carrito")
    private List<ItemCompra> items;
}
