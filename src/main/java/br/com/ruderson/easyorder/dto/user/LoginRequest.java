package br.com.ruderson.easyorder.dto.user;

public record LoginRequest(
    String username,
    String password
) {
}
