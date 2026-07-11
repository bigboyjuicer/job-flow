package ru.rekklez.vacancyservice.authentication.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rekklez.vacancyservice.authentication.service.TokenService;

import javax.crypto.SecretKey;

@Service
public class DefaultTokenService implements TokenService {

    private final SecretKey secretKey;

    public DefaultTokenService(@Value("${ACCESS_SECRET_KEY}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    @Override
    public Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
