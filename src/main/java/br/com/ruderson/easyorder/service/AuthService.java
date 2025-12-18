package br.com.ruderson.easyorder.service;

import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ruderson.easyorder.domain.User;
import br.com.ruderson.easyorder.dto.token.RefreshTokenRequest;
import br.com.ruderson.easyorder.dto.token.TokenResponse;
import br.com.ruderson.easyorder.dto.user.LoginRequest;
import br.com.ruderson.easyorder.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
            SessionService sessionService,
            TokenService tokenService,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.username())
                .orElseThrow(() -> new BadCredentialsException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        SessionService.SessionCreationResult session = sessionService.createSession(user);
        String accessToken = tokenService.generateToken(user, session.sid());

        return new TokenResponse(accessToken, session.refreshToken(), "Bearer", tokenService.getExpirationSeconds());
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        SessionService.SessionData session = sessionService.validateRefreshToken(request.refreshToken());
        User user = userRepository.findById(session.userId())
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));
        String accessToken = tokenService.generateToken(user, session.sid());

        return new TokenResponse(accessToken, request.refreshToken(), "Bearer", tokenService.getExpirationSeconds());
    }

    public void logout(UUID sid) {
        sessionService.deleteSession(sid);
    }
}
