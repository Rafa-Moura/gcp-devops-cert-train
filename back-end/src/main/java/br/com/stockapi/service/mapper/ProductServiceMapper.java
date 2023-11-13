package br.com.stockapi.service.mapper;

import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.dto.response.PageableResponseDto;
import br.com.stockapi.controller.dto.response.ProductResponseDto;
import br.com.stockapi.controller.exception.BusinessException;
import br.com.stockapi.infrastructure.enums.StatusItemEnum;
import br.com.stockapi.infrastructure.model.Product;
import br.com.stockapi.infrastructure.model.StatusProduct;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ProductServiceMapper {

    public static final String ADMIN_USER = "ADMIN";

    public static Product convertDtoToEntity(ProductRequestDto productRequestDto, StatusProduct statusProduct){
        return Product.builder()
                .price(productRequestDto.getPrice())
                .product(productRequestDto.getProduct())
                .quantity(productRequestDto.getQuantity())
                .statusProduct(statusProduct)
                .itemCode(createItemCode())
                .registerUser(ADMIN_USER)
                .build();
    }

    private static String createItemCode(){
        String lettersCode = RandomStringUtils.randomAlphabetic(5).toUpperCase();
        String numbersCode = RandomStringUtils.randomNumeric(5);
        return lettersCode + numbersCode;
    }

    public static PageableResponseDto convertPageProductToPageableResponseDto(Page<Product> products) throws BusinessException {

        return PageableResponseDto.builder()
                .content(convertToProductResponseList(products.getContent()))
                .totalPages(products.getTotalPages())
                .pageNumber(products.getNumber())
                .maxPageContentSize(products.getSize())
                .totalElements(products.getNumberOfElements())
                .firstPage(products.isFirst())
                .lastPage(products.isLast())
                .build();
    }

    public static ProductResponseDto convertProductToDto(Product product) throws BusinessException {
        return ProductResponseDto.builder()
                .product(product.getProduct())
                .quantity(product.getQuantity())
                .itemCode(product.getItemCode())
                .price(product.getPrice())
                .status(StatusItemEnum.getDescriptionByEnumName(product.getStatusProduct().getStatusDescription()))
                .build();
    }

    private static List<ProductResponseDto> convertToProductResponseList(List<Product> products) throws BusinessException {

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for (Product product : products) {
            productResponseDtos.add(convertProductToDto(product));
        }
        return productResponseDtos;
    }
}