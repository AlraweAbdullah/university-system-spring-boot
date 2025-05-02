package be.abdullah.universitysystemspringboot.controllers;

import be.abdullah.universitysystemspringboot.configurations.JwtConfig;
import be.abdullah.universitysystemspringboot.dtos.JwtResponse;
import be.abdullah.universitysystemspringboot.dtos.LoginRequest;
import be.abdullah.universitysystemspringboot.dtos.ProfileDto;
import be.abdullah.universitysystemspringboot.mapper.ProfileMapper;
import be.abdullah.universitysystemspringboot.repositories.ProfileRepository;
import be.abdullah.universitysystemspringboot.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> logIn(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var profile = profileRepository.findByEmail(request.getEmail()).orElseThrow();
        var accesToken = jwtService.generateAccesToken(profile);
        var refreshToken = jwtService.generateRefreshToken(profile);

        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7d
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accesToken));
    }


    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse>validateToken(
            @CookieValue(name = "refreshToken") String refreshToken)
    {
        if(!jwtService.validateToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var profileId = jwtService.getProfileIdFromToken(refreshToken);
        var profile = profileRepository.findById(profileId).orElseThrow();
        var accesToken = jwtService.generateAccesToken(profile);
        return ResponseEntity.ok(new JwtResponse(accesToken));
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileDto> getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var profileId = (Long) authentication.getPrincipal();

        var profile = profileRepository.findById(profileId).orElse(null);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(profileMapper.toDto(profile));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
