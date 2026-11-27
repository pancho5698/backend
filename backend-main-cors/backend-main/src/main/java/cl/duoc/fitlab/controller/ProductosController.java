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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @Operation(summary = "Obtener un producto", description = "Busca un producto por su id.")
    @ApiResponse(responseCode = "200", description = "Producto encontrado.")
    @ApiResponse(responseCode = "404", description = "No existe un producto con ese id.", content = @Content)
    @GetMapping("/{id}")
    public Producto buscar(@PathVariable int id) {
        return servicio.buscar(id);
    }

    @Operation(summary = "Crear un producto", description = "El id lo asigna el backend. Valida precio, oferta, stock y código único.")
    @ApiResponse(responseCode = "201", description = "Producto creado.")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o código repetido.", content = @Content)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto) {
        return servicio.crear(producto);
    }

    @Operation(summary = "Actualizar un producto", description = "Reemplaza los datos del producto con ese id.")
    @ApiResponse(responseCode = "200", description = "Producto actualizado.")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o código repetido.", content = @Content)
    @ApiResponse(responseCode = "404", description = "No existe un producto con ese id.", content = @Content)
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable int id, @RequestBody Producto producto) {
        return servicio.actualizar(id, producto);
    }

    @Operation(summary = "Eliminar un producto", description = "Borra el producto con ese id.")
    @ApiResponse(responseCode = "204", description = "Producto eliminado.")
    @ApiResponse(responseCode = "404", description = "No existe un producto con ese id.", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int id) {
        servicio.eliminar(id);
    }
}
