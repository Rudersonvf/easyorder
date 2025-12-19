package br.com.ruderson.easyorder.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ruderson.easyorder.domain.Product;
import br.com.ruderson.easyorder.dto.product.ProductResponse;
import br.com.ruderson.easyorder.mapper.ProductMapper;
import br.com.ruderson.easyorder.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllProductsByStoreId(UUID storeId, Pageable pageable) {
        Page<Product> products = productRepository.findAllByStore_Id(storeId, pageable);
        return products.map(productMapper::toProductResponse);
    }
}
