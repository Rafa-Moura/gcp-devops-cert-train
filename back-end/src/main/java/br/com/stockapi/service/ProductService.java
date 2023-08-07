package br.com.stockapi.service;

import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.dto.response.PageableResponseDto;
import br.com.stockapi.controller.dto.response.ProductResponseDto;
import br.com.stockapi.controller.exception.NotFoundException;
import br.com.stockapi.infrastructure.enums.StatusItemEnum;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    void insertItem(ProductRequestDto productRequestDto);

    PageableResponseDto getAllProducts(Pageable pageable, StatusItemEnum statusItemEnum);

    ProductResponseDto getProdutcByCode(String code) throws NotFoundException;

    void deleteProduct(String code) throws NotFoundException;

}
