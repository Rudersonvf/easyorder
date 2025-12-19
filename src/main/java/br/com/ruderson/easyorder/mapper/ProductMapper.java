package br.com.ruderson.easyorder.mapper;

import org.mapstruct.Mapper;

import br.com.ruderson.easyorder.domain.Product;
import br.com.ruderson.easyorder.dto.product.ProductResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
}
