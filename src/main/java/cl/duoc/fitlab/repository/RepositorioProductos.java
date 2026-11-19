package cl.duoc.fitlab.repository;

import cl.duoc.fitlab.model.Producto;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

// Spring Data genera la implementación: findAll, save, delete, etc.
public interface RepositorioProductos extends MongoRepository<Producto, ObjectId> {
}
