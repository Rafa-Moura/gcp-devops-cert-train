package br.com.stockapi.service.impl;

import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.dto.response.PageableResponseDto;
import br.com.stockapi.controller.dto.response.ProductResponseDto;
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

import static br.com.stockapi.service.mapper.ProductServiceMapper.convertPageProductToPageableResponseDto;
import static br.com.stockapi.service.mapper.ProductServiceMapper.convertProductToDto;

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

        boolean isNewProduct = productRepository.findByProduct(productRequestDto.getProduct()).isEmpty();

        Product product;
        if(isNewProduct){
            product = ProductServiceMapper.convertDtoToEntity(productRequestDto, statusProduct);
        }else {
            product = productRepository.findByProduct(productRequestDto.getProduct()).get();
            product.setQuantity(product.getQuantity() + productRequestDto.getQuantity());
        }
        productRepository.save(product);

        log.info("Finalizando processo para inserir novo item no estoque: Item [{}] | Código gerado: [{}]", productRequestDto.getProduct(), product.getItemCode());

    }

    @Override
    public PageableResponseDto getAllProducts(Pageable pageable, StatusItemEnum statusItemEnum) {

        StatusProduct statusProduct = statusItemService.getStatusItem(statusItemEnum);

        Page<Product> products = productRepository.findAllByStatusProduct(pageable, statusProduct);

        return convertPageProductToPageableResponseDto(products);
    }

    @Override
    public ProductResponseDto getProdutcByCode(String code) throws NotFoundException {

        Product product =  productRepository.findByItemCode(code).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.toString(),
                "Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente."
        ));

        return convertProductToDto(product);
    }

    @Override
    public void deleteProduct(String code) throws NotFoundException {

        Product product = productRepository.findByItemCode(code).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.toString(),
                "Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente."
        ));

        StatusProduct statusProduct = statusItemService.getStatusItem(StatusItemEnum.REMOVED_STOCK);

        product.setStatusProduct(statusProduct);

        productRepository.save(product);
    }
}
