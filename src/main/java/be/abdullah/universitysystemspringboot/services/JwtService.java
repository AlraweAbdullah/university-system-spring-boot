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

    public Jwt generateAccesToken(Profile profile) {
        return generateToken(profile, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(Profile profile) {
        return generateToken(profile, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateToken(Profile profile, Integer tokenExpiration) {
        var claims = Jwts.claims()
                .subject(profile.getId().toString())
                .add("email", profile.getEmail())
                .add("name", profile.getName())
                .add("role", profile.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * tokenExpiration))
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
    }


    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    public Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        }catch (JwtException e) {
            return null;
        }
    }
}
