package br.com.ruderson.easyorder.dto.order;

import java.math.BigDecimal;

public record OrderResponse(
    Long id,
    String customerName,
    String openedAt,
    String status,
    BigDecimal totalAmount
) {
}
