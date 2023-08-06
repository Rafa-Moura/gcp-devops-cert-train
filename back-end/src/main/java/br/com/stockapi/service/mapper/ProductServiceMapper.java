package br.com.stockapi.service.mapper;

import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.infrastructure.model.Product;
import br.com.stockapi.infrastructure.model.StatusProduct;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

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
}