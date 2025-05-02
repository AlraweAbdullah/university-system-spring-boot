package be.abdullah.universitysystemspringboot.controllers;

import be.abdullah.universitysystemspringboot.dtos.JwtResponse;
import be.abdullah.universitysystemspringboot.dtos.LoginRequest;
import be.abdullah.universitysystemspringboot.dtos.ProfileDto;
import be.abdullah.universitysystemspringboot.mapper.ProfileMapper;
import be.abdullah.universitysystemspringboot.repositories.ProfileRepository;
import be.abdullah.universitysystemspringboot.services.JwtService;
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
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> logIn(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var profile = profileRepository.findByEmail(request.getEmail()).orElseThrow();
        var token = jwtService.generateToken(profile);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    @PostMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String authHeader) {
        var token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileDto> getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var profileId = (Long) authentication.getPrincipal();

        var profile = profileRepository.findById(profileId).orElse(null);
        if(profile == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(profileMapper.toDto(profile));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
