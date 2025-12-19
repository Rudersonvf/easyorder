package br.com.ruderson.easyorder.dto.product;

import java.time.LocalDateTime;

public record ProductResponse(
    Long id,
    String name,
    String description,
    Double price,
    String imageUrl,
    LocalDateTime updatedAt
) {
}
