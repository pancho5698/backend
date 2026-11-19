package cl.duoc.fitlab.controller;

import cl.duoc.fitlab.model.Producto;
import cl.duoc.fitlab.service.ProductosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos") // Versionado de la API pedido en las instrucciones.
@Tag(name = "Productos", description = "Catálogo de la tienda FitLab")
public class ProductosController {
    private final ProductosService servicio;

    public ProductosController(ProductosService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar los productos", description = "Devuelve el catálogo completo ordenado por id.")
    @ApiResponse(responseCode = "200", description = "Lista de productos. Si no hay, devuelve [].",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Producto.class))))
    @GetMapping
    public List<Producto> listar() {
        // Spring convierte la lista a JSON en la respuesta HTTP.
        return servicio.listar();
    }
}
