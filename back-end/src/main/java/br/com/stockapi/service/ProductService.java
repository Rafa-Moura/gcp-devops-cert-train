package br.com.stockapi.service;

import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.exception.NotFoundException;
import br.com.stockapi.infrastructure.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    void insertItem(ProductRequestDto productRequestDto);

    Page<Product> getAllProducts(Pageable pageable);

    Product getProdutcByCode(String code) throws NotFoundException;

    void deleteProduct(String code) throws NotFoundException;

}
