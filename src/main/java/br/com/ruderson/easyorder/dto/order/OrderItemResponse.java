package br.com.ruderson.easyorder.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
    Long id,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    String observations
) {
}
