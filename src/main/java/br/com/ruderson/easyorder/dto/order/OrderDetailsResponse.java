package br.com.ruderson.easyorder.dto.order;

import java.time.LocalDateTime;
import java.util.List;

import br.com.ruderson.easyorder.domain.enums.OrderStatus;
import br.com.ruderson.easyorder.dto.customer.CustomerResponse;

public record OrderDetailsResponse(
    Long id,
    OrderStatus status,
    LocalDateTime openedAt,
    LocalDateTime closedAt,
    List<OrderItemResponse> items,
    CustomerResponse customer
) {
}
