package br.com.ruderson.easyorder.dto.user;

import java.util.UUID;

public record UserResumeDTO(
    UUID id,
    String firstname,
    String lastname,
    String email
) {
}
