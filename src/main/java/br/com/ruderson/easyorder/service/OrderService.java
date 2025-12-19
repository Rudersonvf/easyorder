package br.com.ruderson.easyorder.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ruderson.easyorder.domain.Order;
import br.com.ruderson.easyorder.domain.enums.OrderStatus;
import br.com.ruderson.easyorder.dto.order.OrderResponse;
import br.com.ruderson.easyorder.mapper.OrderMapper;
import br.com.ruderson.easyorder.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> findAllOrders(UUID storeId,
            OrderStatus status,
            String customerName,
            Pageable pageable) {
        
        Page<Order> orders = orderRepository.searchOrders(storeId, status, customerName, pageable);

        return orders.map(orderMapper::toOrderResponse);
    }
}
