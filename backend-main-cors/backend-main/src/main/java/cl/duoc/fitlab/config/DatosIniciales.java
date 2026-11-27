package cl.duoc.fitlab.config;

import cl.duoc.fitlab.model.Usuario;
import cl.duoc.fitlab.repository.RepositorioUsuarios;
import cl.duoc.fitlab.service.UsuariosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Al arrancar, crea el administrador y el vendedor si todavía no existen.
// Sin esto nadie podría entrar como ADMIN, porque el registro público solo crea clientes.
@Component
public class DatosIniciales implements CommandLineRunner {
    private final RepositorioUsuarios repositorio;
    private final UsuariosService usuariosService;
    private final String claveInicial;

    public DatosIniciales(RepositorioUsuarios repositorio, UsuariosService usuariosService,
            @Value("${fitlab.clave-inicial}") String claveInicial) {
        this.repositorio = repositorio;
        this.usuariosService = usuariosService;
        this.claveInicial = claveInicial;
    }

    @Override
    public void run(String... args) {
        crearSiNoExiste("Administrador FitLab", "admin@fitlab.cl", "ADMIN");
        crearSiNoExiste("Vendedor FitLab", "vendedor@fitlab.cl", "VENDEDOR");
    }

    private void crearSiNoExiste(String nombre, String correo, String rol) {
        if (repositorio.findByCorreo(correo).isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setContrasena(claveInicial);
            usuario.setRol(rol);
            usuariosService.crear(usuario);
        }
    }
}
