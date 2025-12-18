package br.com.ruderson.easyorder.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.ruderson.easyorder.domain.User;

@Service
public class SessionService {

    private static final int REFRESH_TOKEN_BYTES = 48;
    private static final long REFRESH_TOKEN_VALID_DAYS = 7L;
    private static final String SESSION_KEY_PREFIX = "session:";
    private static final String USER_SESSION_KEY_PREFIX = "session:user:";
    private static final String REFRESH_KEY_PREFIX = "session:refresh:";

    private final RedisTemplate<String, String> redisTemplate;
    private final SecureRandom secureRandom = new SecureRandom();
    private final MessageDigest sha256;

    public SessionService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        try {
            this.sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    public SessionCreationResult createSession(User user) {
        String existingSid = redisTemplate.opsForValue().get(userSessionKey(user.getId()));
        if (StringUtils.hasText(existingSid)) {
            deleteSession(UUID.fromString(existingSid));
        }

        UUID sid = UUID.randomUUID();
        String refreshToken = generateRefreshToken();
        String refreshTokenHash = hash(refreshToken);
        Instant now = Instant.now();
        Instant expiresAt = now.plus(REFRESH_TOKEN_VALID_DAYS, ChronoUnit.DAYS);
        long ttlSeconds = Math.max(1, ChronoUnit.SECONDS.between(now, expiresAt));

        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        String sessionKey = sessionKey(sid);
        hashOps.put(sessionKey, "userId", user.getId().toString());
        hashOps.put(sessionKey, "refreshTokenHash", refreshTokenHash);
        hashOps.put(sessionKey, "expiresAt", String.valueOf(expiresAt.toEpochMilli()));
        redisTemplate.expire(sessionKey, java.time.Duration.ofSeconds(ttlSeconds));

        redisTemplate.opsForValue().set(userSessionKey(user.getId()), sid.toString(),
                java.time.Duration.ofSeconds(ttlSeconds));
        redisTemplate.opsForValue().set(refreshKey(refreshTokenHash), sid.toString(),
                java.time.Duration.ofSeconds(ttlSeconds));

        return new SessionCreationResult(sid, refreshToken, expiresAt);
    }

    public SessionData validateRefreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new BadCredentialsException("Refresh token inválido");
        }

        String refreshTokenHash = hash(refreshToken);
        String sidStr = redisTemplate.opsForValue().get(refreshKey(refreshTokenHash));
        if (!StringUtils.hasText(sidStr)) {
            throw new BadCredentialsException("Refresh token inválido");
        }

        UUID sid = UUID.fromString(sidStr);
        String sessionKey = sessionKey(sid);
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        String storedHash = (String) hashOps.get(sessionKey, "refreshTokenHash");
        String expiresAtStr = (String) hashOps.get(sessionKey, "expiresAt");
        String userIdStr = (String) hashOps.get(sessionKey, "userId");

        if (!StringUtils.hasText(storedHash) || !StringUtils.hasText(expiresAtStr) || !StringUtils.hasText(userIdStr)) {
            throw new BadCredentialsException("Refresh token inválido");
        }

        Instant expiresAt = Instant.ofEpochMilli(Long.parseLong(expiresAtStr));
        if (expiresAt.isBefore(Instant.now())) {
            deleteSession(sid);
            throw new BadCredentialsException("Refresh token expirado");
        }

        if (!refreshTokenHash.equals(storedHash)) {
            throw new BadCredentialsException("Refresh token inválido");
        }

        return new SessionData(sid, UUID.fromString(userIdStr));
    }

    public boolean isSidActive(UUID sid) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(sessionKey(sid)));
    }

    public void deleteSession(UUID sid) {
        String sessionKey = sessionKey(sid);
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        String userIdStr = (String) hashOps.get(sessionKey, "userId");
        String refreshHash = (String) hashOps.get(sessionKey, "refreshTokenHash");

        redisTemplate.delete(sessionKey);

        if (StringUtils.hasText(userIdStr)) {
            redisTemplate.delete(userSessionKey(UUID.fromString(userIdStr)));
        }
        if (StringUtils.hasText(refreshHash)) {
            redisTemplate.delete(refreshKey(refreshHash));
        }
    }

    private String generateRefreshToken() {
        byte[] randomBytes = new byte[REFRESH_TOKEN_BYTES];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hash(String raw) {
        byte[] digest = sha256.digest(raw.getBytes());
        return Base64.getEncoder().encodeToString(digest);
    }

    private String sessionKey(UUID sid) {
        return SESSION_KEY_PREFIX + sid;
    }

    private String userSessionKey(UUID userId) {
        return USER_SESSION_KEY_PREFIX + userId;
    }

    private String refreshKey(String refreshTokenHash) {
        return REFRESH_KEY_PREFIX + refreshTokenHash;
    }

    public record SessionData(UUID sid, UUID userId) {
    }

    public record SessionCreationResult(UUID sid, String refreshToken, Instant expiresAt) {
    }
}
