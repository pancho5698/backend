package cl.duoc.fitlab.controller;

import cl.duoc.fitlab.model.Categoria;
import cl.duoc.fitlab.service.CategoriasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorías", description = "Categorías del catálogo")
public class CategoriasController {
    private final CategoriasService servicio;

    public CategoriasController(CategoriasService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar las categorías")
    @GetMapping
    public List<Categoria> listar() {
        return servicio.listar();
    }

    @Operation(summary = "Crear una categoría", description = "El nombre es obligatorio y no se puede repetir.")
    @ApiResponse(responseCode = "201", description = "Categoría creada.")
    @ApiResponse(responseCode = "400", description = "Nombre vacío o repetido.", content = @Content)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crear(@RequestBody Categoria categoria) {
        return servicio.crear(categoria);
    }
}
