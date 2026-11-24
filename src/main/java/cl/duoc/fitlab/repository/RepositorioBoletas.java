package cl.duoc.fitlab.repository;

import cl.duoc.fitlab.model.Boleta;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RepositorioBoletas extends MongoRepository<Boleta, ObjectId> {

    @Query("{ 'id': ?0 }")
    Optional<Boleta> buscarPorId(int id);
}
