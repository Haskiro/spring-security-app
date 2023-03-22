package com.github.haskiro.FirstSecurityApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;
    public String generateToken(String username) {
        // срок жизнь токена 60 минут от текущего времени
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)// поле, которое будет хранится в payload(может быть сколько угодно)
                .withIssuedAt(new Date())// дата выдачи токена
                .withIssuer("spring-security-app")// кто выдал токен
                .withExpiresAt(expirationDate)// когда выходит срок
                .sign(Algorithm.HMAC256(secret)); // подпись с секретом
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("spring-security-app")
                .build();

        DecodedJWT jwt = verifier.verify(token); // расшифрованный jwt либо exception
        return jwt.getClaim("username").asString();
    }
}
