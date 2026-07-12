package ru.rekklez.userservice.authentication.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.authentication.service.TokenService;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultTokenService implements TokenService {

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;
    private final long accessTokenTtl;
    private final long refreshTokenTtl;
    private final StringRedisTemplate stringRedisTemplate;

    public DefaultTokenService(
            @Value("${ACCESS_SECRET_KEY}") String accessSecretKey,
            @Value("${REFRESH_SECRET_KEY}") String refreshSecretKey,
            @Value("${ACCESS_TOKEN_TTL}") long accessTokenTtl,
            @Value("${REFRESH_TOKEN_TTL}") long refreshTokenTtl,
            StringRedisTemplate stringRedisTemplate
    ) {
        this.accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey));
        this.refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
        this.accessTokenTtl = accessTokenTtl;
        this.refreshTokenTtl = refreshTokenTtl;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String generateAccessToken(String email, String role, Long id) {
        return generateJwtToken(email, role, id, accessSecretKey, accessTokenTtl);
    }

    @Override
    public String generateRefreshToken(String email, String role, Long id) {
        String refreshToken = generateJwtToken(email, role, id, refreshSecretKey, refreshTokenTtl);
        stringRedisTemplate.opsForValue().set("refresh:" + email, refreshToken, Expiration.from(refreshTokenTtl, TimeUnit.MILLISECONDS));
        return refreshToken;
    }

    @Override
    public boolean refreshTokenIsValid(String refreshToken) {
        String email = extractClaimsFromRefreshToken(refreshToken).getSubject();
        String validRefreshToken = stringRedisTemplate.opsForValue().get("refresh:" + email);
        if(validRefreshToken != null) {
            return validRefreshToken.equals(refreshToken);
        }
        return false;
    }

    @Override
    public void evictRefreshToken(String email) {
        stringRedisTemplate.delete("refresh:" + email);
    }

    private String generateJwtToken(String email, String role, Long id,  SecretKey secretKey, long tokenTtl) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .claim("id", id)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(tokenTtl)))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Claims extractClaimsFromAccessToken(String token) {
        return extractClaims(token, accessSecretKey);
    }

    @Override
    public Claims extractClaimsFromRefreshToken(String token) {
        return extractClaims(token, refreshSecretKey);
    }

    private Claims extractClaims(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
