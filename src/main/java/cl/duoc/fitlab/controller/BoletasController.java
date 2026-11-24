package cl.duoc.fitlab.controller;

import cl.duoc.fitlab.dto.NuevaBoleta;
import cl.duoc.fitlab.model.Boleta;
import cl.duoc.fitlab.service.BoletasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boletas")
@Tag(name = "Boletas", description = "Compras (órdenes) de la tienda")
public class BoletasController {
    private final BoletasService servicio;

    public BoletasController(BoletasService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar las boletas", description = "Las más recientes primero.")
    @GetMapping
    public List<Boleta> listar() {
        return servicio.listar();
    }

    @Operation(summary = "Obtener una boleta con su detalle")
    @ApiResponse(responseCode = "200", description = "Boleta encontrada.")
    @ApiResponse(responseCode = "404", description = "No existe una boleta con ese id.", content = @Content)
    @GetMapping("/{id}")
    public Boleta buscar(@PathVariable int id) {
        return servicio.buscar(id);
    }

    @Operation(summary = "Crear una boleta (comprar)",
            description = "Calcula precios y total con los datos de la base y descuenta el stock.")
    @ApiResponse(responseCode = "201", description = "Compra registrada.")
    @ApiResponse(responseCode = "400", description = "Carrito vacío, cantidad inválida o stock insuficiente.", content = @Content)
    @ApiResponse(responseCode = "404", description = "El cliente o un producto no existe.", content = @Content)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Boleta crear(@RequestBody NuevaBoleta pedido) {
        return servicio.crear(pedido);
    }
}
