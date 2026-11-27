package cl.duoc.fitlab.repository;

import cl.duoc.fitlab.model.Categoria;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioCategorias extends MongoRepository<Categoria, ObjectId> {

    // Sirve para no repetir categorías con el mismo nombre.
    Optional<Categoria> findByNombre(String nombre);
}
