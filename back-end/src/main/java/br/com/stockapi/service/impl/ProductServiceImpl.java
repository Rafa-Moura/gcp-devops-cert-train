package br.com.stockapi.service.impl;

import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.exception.NotFoundException;
import br.com.stockapi.infrastructure.enums.StatusItemEnum;
import br.com.stockapi.infrastructure.model.Product;
import br.com.stockapi.infrastructure.model.StatusProduct;
import br.com.stockapi.infrastructure.repository.ProductRepository;
import br.com.stockapi.service.ProductService;
import br.com.stockapi.service.mapper.ProductServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final StatusProductServiceImpl statusItemService;

    @Override
    public void insertItem(ProductRequestDto productRequestDto) {
        log.info("Iniciando processo para inserir novo item no estoque: Item [{}]", productRequestDto.getProduct());

        StatusProduct statusProduct = statusItemService.getStatusItem(StatusItemEnum.IN_STOCK);

        Product product = ProductServiceMapper.convertDtoToEntity(productRequestDto, statusProduct);

        productRepository.save(product);

        log.info("Finalizando processo para inserir novo item no estoque: Item [{}] | Código gerado: [{}]", productRequestDto.getProduct(), product.getItemCode());

    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProdutcByCode(String code) throws NotFoundException {

        return productRepository.findByItemCode(code).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.toString(),
                "Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente."
        ));
    }

    @Override
    public void deleteProduct(String code) throws NotFoundException {

        Product product = getProdutcByCode(code);
        StatusProduct statusProduct = statusItemService.getStatusItem(StatusItemEnum.REMOVED_STOCK);

        product.setStatusProduct(statusProduct);

        productRepository.save(product);
    }
}
