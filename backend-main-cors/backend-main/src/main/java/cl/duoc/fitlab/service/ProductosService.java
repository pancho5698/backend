package cl.duoc.fitlab.service;

import cl.duoc.fitlab.model.Producto;
import cl.duoc.fitlab.repository.RepositorioProductos;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductosService {
    private final RepositorioProductos repositorio;

    // Inyección por constructor: Spring entrega el repositorio.
    public ProductosService(RepositorioProductos repositorio) {
        this.repositorio = repositorio;
    }

    public List<Producto> listar() {
        return repositorio.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Producto buscar(int id) {
        // Si no existe, Spring responde 404 con este mensaje.
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    public Producto crear(Producto producto) {
        validar(producto);
        if (repositorio.findByCodigo(producto.getCodigo()).isPresent()) {
            throw error("Ya existe un producto con el código " + producto.getCodigo());
        }
        // El id lo asigna el backend: el siguiente número disponible.
        producto.setId(siguienteId());
        return repositorio.save(producto);
    }

    public Producto actualizar(int id, Producto datos) {
        Producto actual = buscar(id);
        validar(datos);
        repositorio.findByCodigo(datos.getCodigo()).ifPresent(otro -> {
            if (otro.getId() != id) {
                throw error("Ya existe un producto con el código " + datos.getCodigo());
            }
        });
        // Conservamos los identificadores para que Mongo reemplace el mismo documento.
        datos.set_id(actual.get_id());
        datos.setId(id);
        return repositorio.save(datos);
    }

    public void eliminar(int id) {
        repositorio.delete(buscar(id));
    }

    // Reglas de negocio: las mismas que valida el formulario del admin en el front.
    private void validar(Producto p) {
        if (p.getCodigo() == null || p.getCodigo().isBlank()) throw error("El código es obligatorio.");
        if (p.getNombre() == null || p.getNombre().isBlank()) throw error("El nombre es obligatorio.");
        if (p.getCategoria() == null || p.getCategoria().isBlank()) throw error("La categoría es obligatoria.");
        if (p.getPrecio() <= 0) throw error("El precio debe ser mayor a 0.");
        if (p.getPrecioOferta() != null && (p.getPrecioOferta() <= 0 || p.getPrecioOferta() >= p.getPrecio())) {
            throw error("La oferta debe ser mayor a 0 y menor que el precio.");
        }
        if (p.getStock() < 0) throw error("El stock no puede ser negativo.");
    }

    private int siguienteId() {
        return repositorio.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .findFirst()
                .map(p -> p.getId() + 1)
                .orElse(1);
    }

    // Error 400: los datos enviados no cumplen las reglas.
    private ResponseStatusException error(String mensaje) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje);
    }
}
