package cl.duoc.fitlab.security;

import cl.duoc.fitlab.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Crea y valida los tokens JWT ("la pulsera" que identifica al usuario en cada petición).
@Service
public class JwtService {
    private final SecretKey clave;
    private final long duracionMs;

    public JwtService(@Value("${jwt.secreto}") String secreto,
            @Value("${jwt.expiracion-minutos}") long minutos) {
        // La clave secreta firma el token: sin ella nadie puede crear uno válido.
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.duracionMs = minutos * 60 * 1000;
    }

    public String generarToken(Usuario usuario) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(usuario.getCorreo())          // quién es
                .claim("rol", usuario.getRol())         // qué puede hacer
                .claim("nombre", usuario.getNombre())
                .claim("id", usuario.getId())
                .issuedAt(ahora)                        // cuándo se creó
                .expiration(new Date(ahora.getTime() + duracionMs)) // cuándo vence
                .signWith(clave)                        // firma con la clave secreta
                .compact();
    }

    // Verifica la firma y la fecha de vencimiento. Si algo falla, lanza una excepción.
    public Claims validarToken(String token) {
        return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload();
    }
}
