package cl.duoc.fitlab.service;

import cl.duoc.fitlab.model.Usuario;
import cl.duoc.fitlab.repository.RepositorioUsuarios;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuariosService {
    // Roles del sistema según las instrucciones de la evaluación.
    private static final List<String> ROLES = List.of("ADMIN", "VENDEDOR", "CLIENTE");

    private final RepositorioUsuarios repositorio;
    // BCrypt transforma la contraseña en un texto irreconocible antes de guardarla.
    private final BCryptPasswordEncoder encriptador = new BCryptPasswordEncoder();

    public UsuariosService(RepositorioUsuarios repositorio) {
        this.repositorio = repositorio;
    }

    public List<Usuario> listar() {
        return repositorio.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Usuario buscar(int id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario buscarPorCorreo(String correo) {
        return repositorio.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un usuario con ese correo"));
    }

    public Usuario crear(Usuario usuario) {
        // Si no se indica rol, el usuario nuevo es cliente.
        if (usuario.getRol() == null) {
            usuario.setRol("CLIENTE");
        }
        validar(usuario, true);
        if (repositorio.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw error("Ya existe una cuenta con ese correo.");
        }
        usuario.setId(siguienteId());
        usuario.setContrasena(encriptador.encode(usuario.getContrasena()));
        return repositorio.save(usuario);
    }

    public Usuario actualizar(int id, Usuario datos) {
        Usuario actual = buscar(id);
        if (datos.getRol() == null) {
            datos.setRol(actual.getRol());
        }
        // Al editar, la contraseña es opcional: si viene vacía se conserva la anterior.
        boolean cambiaContrasena = datos.getContrasena() != null && !datos.getContrasena().isBlank();
        validar(datos, cambiaContrasena);
        repositorio.findByCorreo(datos.getCorreo()).ifPresent(otro -> {
            if (otro.getId() != id) {
                throw error("Ya existe una cuenta con ese correo.");
            }
        });
        datos.setContrasena(cambiaContrasena ? encriptador.encode(datos.getContrasena()) : actual.getContrasena());
        datos.set_id(actual.get_id());
        datos.setId(id);
        return repositorio.save(datos);
    }

    public void eliminar(int id) {
        repositorio.delete(buscar(id));
    }

    // Reglas de negocio: las mismas del formulario de registro del front.
    private void validar(Usuario u, boolean validarContrasena) {
        if (u.getNombre() == null || u.getNombre().isBlank()) throw error("El nombre es obligatorio.");
        if (u.getCorreo() == null || !u.getCorreo().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw error("Ingresa un correo válido.");
        }
        if (validarContrasena && (u.getContrasena() == null || u.getContrasena().length() < 6)) {
            throw error("La contraseña debe tener al menos 6 caracteres.");
        }
        if (!ROLES.contains(u.getRol())) throw error("El rol debe ser ADMIN, VENDEDOR o CLIENTE.");
    }

    private int siguienteId() {
        return repositorio.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .findFirst().map(u -> u.getId() + 1).orElse(1);
    }

    private ResponseStatusException error(String mensaje) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje);
    }
}
