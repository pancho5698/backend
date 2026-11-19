package cl.duoc.fitlab.service;

import cl.duoc.fitlab.model.Producto;
import cl.duoc.fitlab.repository.RepositorioProductos;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
}
