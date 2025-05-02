package be.abdullah.universitysystemspringboot.services;

import be.abdullah.universitysystemspringboot.configurations.JwtConfig;
import be.abdullah.universitysystemspringboot.entities.Profile;
import be.abdullah.universitysystemspringboot.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public String generateAccesToken(Profile profile) {
        return generateToken(profile, jwtConfig.getAccessTokenExpiration());
    }

    public String generateRefreshToken(Profile profile) {
        return generateToken(profile, jwtConfig.getRefreshTokenExpiration());
    }

    private String generateToken(Profile profile, Integer tokenExpiration) {
        return Jwts.builder()
                .subject(profile.getId().toString())
                .claims(Map.of("email", profile.getEmail()))
                .claims(Map.of("name", profile.getName()))
                .claims(Map.of("role", profile.getRole()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * tokenExpiration))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException exception) {
            return false;
        }
    }


    public Long getProfileIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    public Role getRoleFromToken(String token) {
        return Role.valueOf(getClaims(token).get("role", String.class));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }
}
