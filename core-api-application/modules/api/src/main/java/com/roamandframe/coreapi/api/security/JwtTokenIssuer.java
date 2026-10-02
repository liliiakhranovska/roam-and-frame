package com.roamandframe.coreapi.api.security;

import com.roamandframe.coreapi.customer.model.Customer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
    public String issueToken(Customer customer) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(customer.id().toString())
                .claim("email", customer.email())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }
}
