package br.com.ruderson.easyorder.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.ruderson.easyorder.domain.Order;
import br.com.ruderson.easyorder.dto.order.OrderResponse;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "customerName", source = "customer.name")
    OrderResponse toOrderResponse(Order order);
}
