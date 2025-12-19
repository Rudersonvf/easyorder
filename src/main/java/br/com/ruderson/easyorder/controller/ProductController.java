package br.com.ruderson.easyorder.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ruderson.easyorder.dto.product.ProductResponse;
import br.com.ruderson.easyorder.service.ProductService;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable) {
        UUID storeId = UUID.fromString(jwt.getClaimAsString("storeId"));
        return ResponseEntity.ok(productService.findAllProductsByStoreId(storeId, pageable));
    }
}
