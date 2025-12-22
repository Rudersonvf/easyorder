package br.com.ruderson.easyorder.dto.order;

import java.util.List;
import java.util.UUID;

public record OrderRequest(
    UUID customerId,
    List<OrderItemRequest> items
) {
}
