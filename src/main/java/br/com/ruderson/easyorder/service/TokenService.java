package br.com.ruderson.easyorder.service;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.ruderson.easyorder.domain.User;

@Service
public class TokenService {

    private static final long ACCESS_TOKEN_EXPIRATION_SECONDS = 3600L;

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(User user, UUID sid) {
        Instant now = Instant.now();

        String scopes = user.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("easyorder")
                .subject(user.getEmail())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ACCESS_TOKEN_EXPIRATION_SECONDS))
                .claim("scope", scopes)
                .claim("sid", sid.toString())
                .claim("userId", user.getId().toString())
                .claim("storeId", user.getStore().getId().toString())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public long getExpirationSeconds() {
        return ACCESS_TOKEN_EXPIRATION_SECONDS;
    }
}
