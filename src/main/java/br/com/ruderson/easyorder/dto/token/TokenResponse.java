package br.com.ruderson.easyorder.dto.token;

public record TokenResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long expiresIn
) {    
}
