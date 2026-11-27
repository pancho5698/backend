package cl.duoc.fitlab.repository;

import cl.duoc.fitlab.model.Usuario;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RepositorioUsuarios extends MongoRepository<Usuario, ObjectId> {

    @Query("{ 'id': ?0 }")
    Optional<Usuario> buscarPorId(int id);

    // El correo identifica al usuario: no se puede repetir y se usará para el login.
    Optional<Usuario> findByCorreo(String correo);
}
