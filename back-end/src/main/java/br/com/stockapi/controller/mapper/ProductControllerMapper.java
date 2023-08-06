package br.com.stockapi.controller.mapper;

import br.com.stockapi.controller.dto.response.PageableResponseDto;
import br.com.stockapi.controller.dto.response.ProductResponseDto;
import br.com.stockapi.infrastructure.model.Product;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ProductControllerMapper {

    public static PageableResponseDto convertPageProductToPageableResponseDto(Page<Product> products) {

        return PageableResponseDto.builder()
                .content(convertToProductResponseList(products.getContent()))
                .totalPages(products.getTotalPages())
                .pageNumber(products.getNumber())
                .maxPageContentSize(products.getSize())
                .firstPage(products.isFirst())
                .lastPage(products.isLast())
                .build();
    }

    public static ProductResponseDto convertProductToDto(Product product) {
        return ProductResponseDto.builder()
                .product(product.getProduct())
                .quantity(product.getQuantity())
                .itemCode(product.getItemCode())
                .price(product.getPrice())
                .build();
    }

    public static List<ProductResponseDto> convertToProductResponseList(List<Product> products) {

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for (Product product : products) {
            productResponseDtos.add(ProductResponseDto.builder()
                    .product(product.getProduct())
                    .itemCode(product.getItemCode())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .build());
        }
        return productResponseDtos;
    }
}