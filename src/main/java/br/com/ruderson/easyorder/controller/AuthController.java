package br.com.ruderson.easyorder.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruderson.easyorder.dto.token.RefreshTokenRequest;
import br.com.ruderson.easyorder.dto.token.TokenResponse;
import br.com.ruderson.easyorder.dto.user.LoginRequest;
import br.com.ruderson.easyorder.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.authenticate(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = authService.refreshToken(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(JwtAuthenticationToken authentication) {
        if (authentication == null) {
            return ResponseEntity.noContent().build();
        }

        String sidClaim = authentication.getToken().getClaimAsString("sid");
        if (sidClaim != null) {
            try {
                authService.logout(UUID.fromString(sidClaim));
            } catch (IllegalArgumentException ignored) {
                // if sid is malformed just ignore logout to avoid exposing details
            }
        }
        return ResponseEntity.noContent().build();
    }
}
