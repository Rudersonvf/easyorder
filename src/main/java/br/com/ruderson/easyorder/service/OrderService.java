package br.com.ruderson.easyorder.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ruderson.easyorder.domain.Order;
import br.com.ruderson.easyorder.domain.OrderItem;
import br.com.ruderson.easyorder.domain.OrderItemId;
import br.com.ruderson.easyorder.domain.Product;
import br.com.ruderson.easyorder.domain.enums.OrderStatus;
import br.com.ruderson.easyorder.dto.order.OrderItemRequest;
import br.com.ruderson.easyorder.dto.order.OrderDetailsResponse;
import br.com.ruderson.easyorder.dto.order.OrderRequest;
import br.com.ruderson.easyorder.dto.order.OrderResponse;
import br.com.ruderson.easyorder.mapper.OrderMapper;
import br.com.ruderson.easyorder.repository.CustomerRepository;
import br.com.ruderson.easyorder.repository.OrderRepository;
import br.com.ruderson.easyorder.repository.ProductRepository;
import br.com.ruderson.easyorder.repository.StoreRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            StoreRepository storeRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.storeRepository = storeRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> findAllOrders(UUID storeId,
            OrderStatus status,
            String customerName,
            Pageable pageable) {
        
        Page<Order> orders = orderRepository.searchOrders(storeId, status, customerName, pageable);

        return orders.map(orderMapper::toOrderResponse);
    }

    @Transactional(readOnly = true)
    public OrderDetailsResponse findOrderDetails(UUID storeId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getStore().getId().equals(storeId))
                .orElseThrow(() -> new IllegalArgumentException("Pedido com o ID " + orderId + " não encontrado"));

        return orderMapper.toOrderDetailsResponse(order);
    }

    @Transactional
    public OrderResponse createOrder(UUID storeId, OrderRequest orderRequest) {
        if (orderRequest.items() == null || orderRequest.items().isEmpty()) {
            throw new IllegalArgumentException("Pedido precisa ter pelo menos um item");
        }

        Order order = orderMapper.toOrder(orderRequest);
        order.setStore(storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Loja não encontrada")));

        if (orderRequest.customerId() != null) {
            order.setCustomer(customerRepository.findById(orderRequest.customerId())
                    .filter(customer -> customer.getStore().getId().equals(storeId))
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado")));
        }

        Order savedOrder = orderRepository.save(order);

        Set<OrderItem> items = new HashSet<>();
        for (OrderItemRequest itemRequest : orderRequest.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .filter(p -> p.getStore().getId().equals(storeId))
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

            OrderItem item = orderMapper.toOrderItem(itemRequest);
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
            item.setId(new OrderItemId(savedOrder.getId(), product.getId()));
            items.add(item);
        }
        savedOrder.setItems(items);

        Order savedWithItems = orderRepository.save(savedOrder);
        return orderMapper.toOrderResponse(savedWithItems);
    }
}
