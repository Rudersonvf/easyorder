package br.com.ruderson.easyorder.dto.store;

import java.util.UUID;

public record StoreResumeDTO(
    UUID id,
    String name,
    String cnpj
  //String imageUrl
) {
}
