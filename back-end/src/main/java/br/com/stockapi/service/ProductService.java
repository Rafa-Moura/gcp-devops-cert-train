package br.com.stockapi.service;

import br.com.stockapi.controller.dto.request.ProductOutputRequestDto;
import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.dto.response.PageableResponseDto;
import br.com.stockapi.controller.dto.response.ProductResponseDto;
import br.com.stockapi.controller.exception.BusinessException;
import br.com.stockapi.controller.exception.NotFoundException;
import br.com.stockapi.infrastructure.enums.StatusItemEnum;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    void insertItem(ProductRequestDto productRequestDto) throws NotFoundException;

    PageableResponseDto getAllProducts(Pageable pageable, StatusItemEnum statusItemEnum) throws NotFoundException, BusinessException;

    ProductResponseDto getProductByCode(String code) throws NotFoundException, BusinessException;

    void deleteProduct(String code) throws NotFoundException;

    ProductResponseDto outputProduct(ProductOutputRequestDto productOutputRequestDto) throws NotFoundException, BusinessException;

}
