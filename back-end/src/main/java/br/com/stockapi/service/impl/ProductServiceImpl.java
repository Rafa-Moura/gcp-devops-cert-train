package br.com.stockapi.service.impl;

import br.com.stockapi.controller.dto.request.ProductOutputRequestDto;
import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.dto.response.PageableResponseDto;
import br.com.stockapi.controller.dto.response.ProductResponseDto;
import br.com.stockapi.controller.exception.BusinessException;
import br.com.stockapi.controller.exception.NotFoundException;
import br.com.stockapi.infrastructure.enums.StatusItemEnum;
import br.com.stockapi.infrastructure.model.Product;
import br.com.stockapi.infrastructure.model.StatusProduct;
import br.com.stockapi.infrastructure.repository.ProductRepository;
import br.com.stockapi.service.ProductService;
import br.com.stockapi.service.StatusProductService;
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

    private final StatusProductService statusItemService;

    @Override
    public void insertItem(ProductRequestDto productRequestDto) throws NotFoundException {
        log.info("Iniciando processo para inserir novo item no estoque: Item [{}]", productRequestDto.getProduct());

        StatusProduct statusProduct = statusItemService.getStatusItem(StatusItemEnum.IN_STOCK);

        boolean isNewProduct = productRepository.findByProduct(productRequestDto.getProduct()).isEmpty();

        Product product;
        if (isNewProduct) {
            product = ProductServiceMapper.convertDtoToEntity(productRequestDto, statusProduct);
            log.info("O produto: [{}] informado não possuí cadastro no estoque. realizado novo registro.", product.getProduct());
        } else {
            product = productRepository.findByProduct(productRequestDto.getProduct()).get();
            product.setQuantity(product.getQuantity() + productRequestDto.getQuantity());
            log.info("O produto: [{}] informado possuí cadastro no estoque. realizada atualização de dados do produto.",
                    product.getProduct());
        }

        productRepository.save(product);
        log.info("Finalizando processo para inserir novo item no estoque: Item [{}] | Código: [{}]", productRequestDto.getProduct(), product.getItemCode());

    }

    @Override
    public PageableResponseDto getAllProducts(Pageable pageable, StatusItemEnum statusItemEnum) throws NotFoundException, BusinessException {

        log.info("Iniciando busca de todos os produtos no estoque");

        StatusProduct statusProduct = statusItemService.getStatusItem(statusItemEnum);

        Page<Product> products;

        if (statusProduct == null) {
            products = productRepository.findAll(pageable);
            log.info("Total de itens localizados no estoque: [{}]", products.getTotalElements());
            return convertPageProductToPageableResponseDto(products);
        }

        products = productRepository.findAllByStatusProduct(pageable, statusProduct);

        log.info("Total de itens localizados no estoque: [{}] com o status: [{}]", products.getTotalElements(),
                statusItemEnum.getDescription());

        return convertPageProductToPageableResponseDto(products);
    }

    @Override
    public ProductResponseDto getProductByCode(String code) throws NotFoundException, BusinessException {

        log.info("Iniciando busca de produto por código. Código: [{}]", code);

        Product product = getProduct(code);

        log.info("Produto: [{}] foi localizado com o código: [{}]", product.getProduct(), code);
        return convertProductToDto(product);
    }

    @Override
    public void deleteProduct(String code) throws NotFoundException {

        log.info("Iniciando remoção de item do estoque por código. Código informado: [{}]", code);

        Product product = getProduct(code);

        StatusProduct statusProduct = statusItemService.getStatusItem(StatusItemEnum.REMOVED_STOCK);

        product.setStatusProduct(statusProduct);

        log.info("Produto: [{}] localizado pelo código: [{}] foi removido com sucesso.", product.getProduct(), code);
        productRepository.save(product);
    }

    @Override
    public ProductResponseDto outputProduct(ProductOutputRequestDto productOutputRequestDto) throws NotFoundException, BusinessException {

        log.info("Iniciando camada de serviço para registrar saída do Produto: [{}] Quantidade de itens que serão removidos: [{}]",
                productOutputRequestDto.getCode(), productOutputRequestDto.getQuantity());

        Product product = getProduct(productOutputRequestDto.getCode());

        if (productOutputRequestDto.getQuantity() > product.getQuantity()) {

            log.error("Erro ao registrar a saída do produto. A quantidade informada: [{}] é maior que a quantidade atual de itens em estoque: [{}]",
                    productOutputRequestDto.getQuantity(), product.getQuantity());

            throw new BusinessException(HttpStatus.BAD_REQUEST.toString(),
                    "A quantidade de produto informada para saída é maior que o total em estoque atual.");
        }

        if (!product.getStatusProduct().getStatusDescription().equalsIgnoreCase(StatusItemEnum.IN_STOCK.name())) {

            log.error("Erro ao registrar a saída do produto. O produto informado está com status: [{}]",
                    product.getStatusProduct().getStatusDescription());

            throw new BusinessException(HttpStatus.BAD_REQUEST.toString(),
                    "Produto indisponível para saída. Favor verificar o status do item no estoque " +
                            "e tentar novamente. Apenas itens com status Em estoque podem ser retirados do estoque");
        }

        product.setQuantity(product.getQuantity() - productOutputRequestDto.getQuantity());

        if (product.getQuantity() == 0) {

            log.warn("Produto: [{}] após a venda ficará sem unidades disponíveis no estoque.", product.getProduct());

            StatusProduct statusProduct = statusItemService.getStatusItem(StatusItemEnum.OUT_STOCK);
            product.setStatusProduct(statusProduct);
        }

        log.info("Finalizando camada de serviço para registrar saída do Produto: [{}] com o código [{}] Quantidade de itens no estoque atual: [{}]",
                product.getProduct(), product.getItemCode(), product.getQuantity());

        productRepository.save(product);
        return convertProductToDto(product);
    }

    private Product getProduct(String code) throws NotFoundException {
        return productRepository.findByItemCode(code).orElseThrow(() -> {
            log.error("Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente. Código informado: [{}]", code);
            return new NotFoundException(HttpStatus.NOT_FOUND.toString(), "Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente.");
        });
    }
}
