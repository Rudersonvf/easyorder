package br.com.ruderson.easyorder.dto.customer;

import java.util.UUID;

public record CustomerResponse(
    UUID id,
    String name,
    String phoneNumber
) {
}
