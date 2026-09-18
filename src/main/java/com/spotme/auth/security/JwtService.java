package com.spotme.auth.security;

import com.spotme.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Issues and validates JWTs.
 * <p>
 * Tokens are stateless and carry the user's id ({@code sub}) and
 * {@code username} as claims, signed with HMAC-SHA256, valid for 7 days.
 */
@Service
public class JwtService {

    private static final long EXPIRATION_MILLIS = 7L * 24 * 60 * 60 * 1000; // 7 days

    private final SecretKey signingKey;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates a signed JWT for the given user.
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MILLIS);

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extracts the user id ({@code sub} claim) from a valid token.
     * @throws io.jsonwebtoken.JwtException if the token is invalid, malformed, or expired
     */
    public Long extractUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * Extracts the {@code username} claim from a valid token.
     */
    public String extractUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
