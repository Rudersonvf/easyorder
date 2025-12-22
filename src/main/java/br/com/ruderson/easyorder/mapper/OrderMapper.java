package br.com.ruderson.easyorder.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.ruderson.easyorder.domain.Order;
import br.com.ruderson.easyorder.domain.OrderItem;
import br.com.ruderson.easyorder.dto.order.OrderDetailsResponse;
import br.com.ruderson.easyorder.dto.order.OrderItemResponse;
import br.com.ruderson.easyorder.dto.order.OrderItemRequest;
import br.com.ruderson.easyorder.dto.order.OrderRequest;
import br.com.ruderson.easyorder.dto.order.OrderResponse;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "customerName", source = "customer.name")
    OrderResponse toOrderResponse(Order order);

    OrderDetailsResponse toOrderDetailsResponse(Order order);

    @Mapping(target = "id", source = "id.productId")
    @Mapping(target = "productName", source = "product.name")
    OrderItemResponse toOrderItemResponse(OrderItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "openedAt", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toOrder(OrderRequest orderRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    OrderItem toOrderItem(OrderItemRequest item);
}
