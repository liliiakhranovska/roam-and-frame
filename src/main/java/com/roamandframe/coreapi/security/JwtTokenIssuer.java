package com.roamandframe.coreapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
class JwtTokenIssuer implements TokenIssuer {

    private final SecretKey key;
    private final long expirationMinutes;

    JwtTokenIssuer(@Value("${app.jwt.secret}") String secret,
                   @Value("${app.jwt.expiration-minutes}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String issueToken(UUID customerId, String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(customerId.toString())
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }
}
