package br.com.stockapi.controller;

import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.dto.response.PageableResponseDto;
import br.com.stockapi.controller.dto.response.ProductResponseDto;
import br.com.stockapi.controller.exception.NotFoundException;
import br.com.stockapi.infrastructure.model.Product;
import br.com.stockapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.com.stockapi.controller.mapper.ProductControllerMapper.convertPageProductToPageableResponseDto;
import static br.com.stockapi.controller.mapper.ProductControllerMapper.convertProductToDto;

@RestController
@RequestMapping(value = "/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void insertItem(@RequestBody @Valid ProductRequestDto productRequestDto) {

        log.info("Iniciando controller de inserção de novo item no estoque. Item: [{}]", productRequestDto.getProduct());

        service.insertItem(productRequestDto);

        log.info("Finalizando controller de inserção de novo item no estoque. Item: [{}]", productRequestDto.getProduct());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseDto> getAllProducts(Pageable pageable) {

        log.info("Iniciando controller de listagem de produtos");

        Page<Product> products = service.getAllProducts(pageable);

        PageableResponseDto pageableResponseDto = convertPageProductToPageableResponseDto(products);

        log.info("Finalizando controller de listagem de produtos");

        return new ResponseEntity<>(pageableResponseDto, HttpStatus.OK);

    }

    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDto> getProductByCode(@PathVariable String code) throws NotFoundException {
        log.info("Iniciando controller de visualizar detalhes de um produto. Código do produto: [{}]", code);

        Product product = service.getProdutcByCode(code);

        ProductResponseDto productResponseDto = convertProductToDto(product);

        log.info("Finalizando controller de visualizar detalhes de um produto. Código do produto: [{}]", code);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);

    }

    @DeleteMapping(value = "/{code}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String code) throws NotFoundException {
        log.info("Iniciando controller de visualizar detalhes de um produto. Código do produto: [{}]", code);

        service.deleteProduct(code);

        log.info("Finalizando controller de visualizar detalhes de um produto. Código do produto: [{}]", code);
    }
}
