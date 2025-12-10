package com.micropos.mapper;

import com.micropos.dto.ProductDto;
import com.micropos.model.Product;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(implementationPackage = "com.micropos.products.mapper")
public interface ProductMapper {

    Collection<ProductDto> toProductsDto(Collection<Product> products);

    Collection<Product> toProducts(Collection<ProductDto> products);

    Product toProduct(ProductDto productDto);

    ProductDto toProductDto(Product pet);
}
