package br.app.tads.clinica_facil.infra.security;

import br.app.tads.clinica_facil.model.interfaces.IUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("{API.SECURITY.TOKEN.SECRET}")
    private String secret;

    public String generateToken(IUser user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("clinica-facil-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpireDate())
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao autenticar.", e);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("clinica-facil-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException e) {
            // Token expirou
            throw new TokenExpiredException("Token expirado", Instant.now());

        }catch (JWTVerificationException e) {
            return null;
        }
    }

    private Instant generateExpireDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3"));
    }

}
