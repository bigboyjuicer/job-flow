package ru.rekklez.userservice.authentication.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.authentication.service.JwtService;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;

@Service
public class DefaultJwtService implements JwtService {

    private final SecretKey secretKey;

    public DefaultJwtService(@Value("${JWT_SECRET_KEY}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateJwt(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(60 * 5)))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


}
