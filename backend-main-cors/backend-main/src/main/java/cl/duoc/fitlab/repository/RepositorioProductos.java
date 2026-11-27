package cl.duoc.fitlab.repository;

import cl.duoc.fitlab.model.Producto;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

// Spring Data genera la implementación: findAll, save, delete, etc.
public interface RepositorioProductos extends MongoRepository<Producto, ObjectId> {

    // Busca por nuestro campo "id" (1, 2, 3...), no por el _id interno de Mongo.
    @Query("{ 'id': ?0 }")
    Optional<Producto> buscarPorId(int id);

    // Spring arma la consulta a partir del nombre del método: { codigo: ? }
    Optional<Producto> findByCodigo(String codigo);
}
