package br.com.ruderson.easyorder.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.ruderson.easyorder.domain.enums.OrderStatus;
import br.com.ruderson.easyorder.dto.order.OrderDetailsResponse;
import br.com.ruderson.easyorder.dto.order.OrderRequest;
import br.com.ruderson.easyorder.dto.order.OrderResponse;
import br.com.ruderson.easyorder.service.OrderService;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(name = "customerName", defaultValue = "") String customerName,
            Pageable pageable) {

        UUID storeId = UUID.fromString(jwt.getClaimAsString("storeId"));
        return ResponseEntity.ok(orderService.findAllOrders(storeId, status, customerName, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id) {

        UUID storeId = UUID.fromString(jwt.getClaimAsString("storeId"));
        return ResponseEntity.ok(orderService.findOrderDetails(storeId, id));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody OrderRequest orderRequest
    ) {
        UUID storeId = UUID.fromString(jwt.getClaimAsString("storeId"));
        OrderResponse orderResponse = orderService.createOrder(storeId, orderRequest);
        return ResponseEntity.created(
        	ServletUriComponentsBuilder.fromCurrentRequest()
        		.path("/{id}")
        		.buildAndExpand(orderResponse.id())
        		.toUri()
        ).body(orderResponse);
    }
}