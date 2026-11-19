package cl.duoc.fitlab;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "API FitLab",
        description = "API de la tienda FitLab: productos, usuarios y boletas.",
        version = "1.0"))
public class FitlabApplication {
    public static void main(String[] args) {
        // Spring inicia el servidor HTTP y detecta los componentes del paquete.
        SpringApplication.run(FitlabApplication.class, args);
    }
}
