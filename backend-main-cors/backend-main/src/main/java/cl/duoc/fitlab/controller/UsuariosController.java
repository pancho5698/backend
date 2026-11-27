package cl.duoc.fitlab.controller;

import cl.duoc.fitlab.model.Usuario;
import cl.duoc.fitlab.service.UsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Usuarios y roles del sistema")
public class UsuariosController {
    private final UsuariosService servicio;

    public UsuariosController(UsuariosService servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Listar los usuarios", description = "Nunca devuelve las contraseñas.")
    @GetMapping
    public List<Usuario> listar() {
        return servicio.listar();
    }

    @Operation(summary = "Obtener un usuario")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado.")
    @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id.", content = @Content)
    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable int id) {
        return servicio.buscar(id);
    }

    @Operation(summary = "Crear un usuario", description = "Encripta la contraseña. Si no se indica rol, queda como CLIENTE.")
    @ApiResponse(responseCode = "201", description = "Usuario creado.")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o correo repetido.", content = @Content)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crear(@RequestBody Usuario usuario) {
        return servicio.crear(usuario);
    }

    @Operation(summary = "Actualizar un usuario", description = "Si la contraseña viene vacía, se conserva la anterior.")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado.")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o correo repetido.", content = @Content)
    @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id.", content = @Content)
    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable int id, @RequestBody Usuario usuario) {
        return servicio.actualizar(id, usuario);
    }

    @Operation(summary = "Eliminar un usuario")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado.")
    @ApiResponse(responseCode = "404", description = "No existe un usuario con ese id.", content = @Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int id) {
        servicio.eliminar(id);
    }
}
