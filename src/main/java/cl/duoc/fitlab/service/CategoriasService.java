package cl.duoc.fitlab.service;

import cl.duoc.fitlab.model.Categoria;
import cl.duoc.fitlab.repository.RepositorioCategorias;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoriasService {
    private final RepositorioCategorias repositorio;

    public CategoriasService(RepositorioCategorias repositorio) {
        this.repositorio = repositorio;
    }

    public List<Categoria> listar() {
        return repositorio.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Categoria crear(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio.");
        }
        if (repositorio.findByNombre(categoria.getNombre()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La categoría ya existe.");
        }
        // Siguiente id disponible, igual que en productos.
        int siguienteId = repositorio.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .findFirst().map(c -> c.getId() + 1).orElse(1);
        categoria.setId(siguienteId);
        return repositorio.save(categoria);
    }
}
