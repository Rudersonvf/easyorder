package br.com.ruderson.easyorder.dto.order;

public record OrderItemRequest(
    Long productId,
    Integer quantity,
    String observations
) {
}
