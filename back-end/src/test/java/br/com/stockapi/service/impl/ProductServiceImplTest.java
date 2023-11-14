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
import br.com.stockapi.service.StatusProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    @Mock
    private StatusProductService statusProductService;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deverá inserir um novo produto no sistema")
    void mustBeInsertANewItemSuccess() throws NotFoundException {

        when(statusProductService.getStatusItem(Mockito.any())).thenReturn(createAStatusProduct(StatusItemEnum.IN_STOCK));

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(createAProduct(StatusItemEnum.IN_STOCK, 40));
        when(productRepository.findByProduct(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertDoesNotThrow(() -> productService.insertItem(createAProductRequestDto()));

        Mockito.verify(productRepository, times(1)).save(Mockito.any(Product.class));
        Mockito.verify(productRepository, times(1)).findByProduct(Mockito.anyString());
        Mockito.verify(statusProductService, times(1)).getStatusItem(Mockito.any());
    }

    @Test
    @DisplayName("Deverá atualizar um produto já existente no sistema")
    void mustBeUpdateAnItemSuccess() throws NotFoundException {

        Product product = createAProduct(StatusItemEnum.IN_STOCK, 10);
        product.setItemCode("AAAFF22233");

        when(statusProductService.getStatusItem(Mockito.any())).thenReturn(createAStatusProduct(StatusItemEnum.IN_STOCK));

        when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        when(productRepository.findByProduct(Mockito.anyString())).thenReturn(Optional.of(product));

        Assertions.assertDoesNotThrow(() -> productService.insertItem(createAProductRequestDto()));

        Mockito.verify(productRepository, times(1)).save(Mockito.any(Product.class));
        Mockito.verify(productRepository, times(2)).findByProduct(Mockito.anyString());
        Mockito.verify(statusProductService, times(1)).getStatusItem(Mockito.any());
    }

    @Test
    @DisplayName("Deverá retornar um PageableResponseDto contendo 1 item no content")
    void mustBeReturnPageableResponseDtoSuccess() throws NotFoundException, BusinessException {

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(createAProductList(), pageRequest, createAProductList().size());

        when(statusProductService.getStatusItem(Mockito.any())).thenReturn(createAStatusProduct(null));
        when(productRepository.findAll(Mockito.any(Pageable.class))).thenReturn(productPage);

        PageableResponseDto responseDto = productService.getAllProducts(pageRequest, null);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(createAProductList().get(0).getId(), productPage.getContent().get(0).getId());
        Assertions.assertEquals(createAProductList().get(0).getItemCode(), productPage.getContent().get(0).getItemCode());
        Assertions.assertEquals(createAProductList().get(0).getProduct(), productPage.getContent().get(0).getProduct());
        Assertions.assertEquals(createAProductList().get(0).getQuantity(), productPage.getContent().get(0).getQuantity());
        Assertions.assertEquals(createAProductList().get(0).getPrice(), productPage.getContent().get(0).getPrice());
        Assertions.assertEquals(createAProductList().get(0).getRegisterUser(), productPage.getContent().get(0).getRegisterUser());
        Assertions.assertEquals(createAProductList().get(0).getStatusProduct(), productPage.getContent().get(0).getStatusProduct());

        Mockito.verify(statusProductService, times(1)).getStatusItem(Mockito.any());
        Mockito.verify(productRepository, times(1)).findAll(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Deverá retornar um PageableResponseDto contendo 2 itens no content filtrados pelo status OUT")
    void mustBeReturnPageableResponseDtoSuccessFilteredByStatus() throws NotFoundException, BusinessException {

        List<Product> products = new ArrayList<>();
        products.add(createAProduct(StatusItemEnum.OUT_STOCK, 0));
        products.add(createAProduct(StatusItemEnum.OUT_STOCK, 0));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(products, pageRequest, products.size());

        when(statusProductService.getStatusItem(Mockito.any())).thenReturn(createAStatusProduct(StatusItemEnum.OUT_STOCK));

        when(productRepository.findAllByStatusProduct(Mockito.any(Pageable.class), Mockito.any())).thenReturn(productPage);

        PageableResponseDto responseDto = productService.getAllProducts(pageRequest, StatusItemEnum.OUT_STOCK);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(2, responseDto.getContent().size());
        Assertions.assertEquals(products.get(0).getId(), productPage.getContent().get(0).getId());
        Assertions.assertEquals(products.get(0).getItemCode(), productPage.getContent().get(0).getItemCode());
        Assertions.assertEquals(products.get(0).getProduct(), productPage.getContent().get(0).getProduct());
        Assertions.assertEquals(products.get(0).getQuantity(), productPage.getContent().get(0).getQuantity());
        Assertions.assertEquals(products.get(0).getPrice(), productPage.getContent().get(0).getPrice());
        Assertions.assertEquals(products.get(0).getRegisterUser(), productPage.getContent().get(0).getRegisterUser());
        Assertions.assertEquals(products.get(0).getStatusProduct(), productPage.getContent().get(0).getStatusProduct());

        Mockito.verify(statusProductService, times(1)).getStatusItem(Mockito.any());
        Mockito.verify(productRepository, times(1)).findAllByStatusProduct(Mockito.any(Pageable.class), Mockito.any());
    }

    @Test
    @DisplayName(value = "Deverá retornar um único produto localizado pelo seu código")
    void mustBeReturnASingleProductByCode() throws BusinessException, NotFoundException {

        Product product = createAProduct(StatusItemEnum.IN_STOCK, 10);
        product.setItemCode("ASDQQR23145");

        when(productRepository.findByItemCode(Mockito.anyString())).thenReturn(Optional.of(product));

        ProductResponseDto productResponseDto = productService.getProductByCode(product.getItemCode());

        Assertions.assertEquals(product.getProduct(), productResponseDto.getProduct());
        Assertions.assertEquals(product.getItemCode(), productResponseDto.getItemCode());
        Assertions.assertEquals(product.getQuantity(), productResponseDto.getQuantity());
        Assertions.assertEquals(product.getPrice(), productResponseDto.getPrice());

        Mockito.verify(productRepository, times(1)).findByItemCode(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deverá retornar um NotFoundException quando não localizar o item pelo código")
    void mustBeReturnANotFoundExceptionWhenCodeIsInvalid(){

        when(productRepository.findByItemCode(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable error = Assertions.assertThrows(NotFoundException.class, () -> {
                productService.getProductByCode("ASD2314");
        });

        Assertions.assertNotNull(error);
        Assertions.assertEquals("Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente.", error.getMessage());
        Mockito.verify(productRepository, times(1)).findByItemCode(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deverá deletar logicamente, atualizando o seu status para REMOVED_STOCK com sucesso")
    void mustBeDeleteProductSuccess(){

        Product product = createAProduct(StatusItemEnum.IN_STOCK, 10);

        when(productRepository.findByItemCode(Mockito.anyString())).thenReturn(Optional.of(product));
        when(productRepository.save(Mockito.any())).thenReturn(product);

        Assertions.assertDoesNotThrow(() -> productService.deleteProduct("ADWEQ21543"));

        Mockito.verify(productRepository, times(1)).findByItemCode(Mockito.anyString());
        Mockito.verify(productRepository, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName(value = "Deverá retornar um NotFoundException quando não localizar o item pelo código para realizar a remoção do mesmo")
    void mustBeReturnANotFoundExceptionWhenCodeIsInvalidToDelete(){

        when(productRepository.findByItemCode(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable error = Assertions.assertThrows(NotFoundException.class, () -> {
            productService.deleteProduct("ADDER12142");
        });

        Assertions.assertNotNull(error);
        Assertions.assertEquals("Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente.", error.getMessage());
        Mockito.verify(productRepository, times(1)).findByItemCode(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName(value = "Deverá realizar a saída de um determinado produto do banco de dados e retornar que restam 7 itens no estoque")
    void mustBeOutputAItemAndReturnQuantityIs7() throws BusinessException, NotFoundException {

        ProductOutputRequestDto productOutputRequestDto = ProductOutputRequestDto.builder()
                .code("ADETG31532")
                .quantity(3)
                .build();

        when(productRepository.findByItemCode(Mockito.anyString())).thenReturn(Optional.of(createAProduct(StatusItemEnum.IN_STOCK, 10)));

        ProductResponseDto productResponseDto = productService.outputProduct(productOutputRequestDto);

        Assertions.assertEquals(7, productResponseDto.getQuantity());
        Assertions.assertEquals("Em estoque", productResponseDto.getStatus());
        Mockito.verify(productRepository, times(1)).findByItemCode(Mockito.anyString());

    }

    @Test
    @DisplayName(value = "Deverá realizar a saída de um determinado produto do banco de dados e retornar que não restam itens no estoque e status Sem unidade no estoque")
    void mustBeOutputAItemAndReturnQuantityIs0AndStatusOut() throws BusinessException, NotFoundException {

        ProductOutputRequestDto productOutputRequestDto = ProductOutputRequestDto.builder()
                .code("ADETG31532")
                .quantity(10)
                .build();

        when(productRepository.findByItemCode(Mockito.anyString())).thenReturn(Optional.of(createAProduct(StatusItemEnum.IN_STOCK, 10)));
        when(statusProductService.getStatusItem(Mockito.any())).thenReturn(createAStatusProduct(StatusItemEnum.OUT_STOCK));

        ProductResponseDto productResponseDto = productService.outputProduct(productOutputRequestDto);

        Assertions.assertEquals(0, productResponseDto.getQuantity());
        Assertions.assertEquals("Sem unidade no estoque", productResponseDto.getStatus());

        Mockito.verify(productRepository, times(1)).findByItemCode(Mockito.anyString());
        Mockito.verify(statusProductService, times(1)).getStatusItem(Mockito.any());

    }

    @Test
    @DisplayName(value = "Deverá retornar um erro quando tentar realizar a retirada de um item que não possui unidades disponíveis no estoque")
    void mustBeReturnExceptionWhenTryToOutputAItemWithNoUnitsInStock() throws BusinessException, NotFoundException {

        ProductOutputRequestDto productOutputRequestDto = ProductOutputRequestDto.builder()
                .code("ADETG31532")
                .quantity(10)
                .build();

        when(productRepository.findByItemCode(Mockito.anyString())).thenReturn(Optional.of(createAProduct(StatusItemEnum.OUT_STOCK, 0)));

        Throwable error= Assertions.assertThrows(BusinessException.class, () -> {
            productService.outputProduct(productOutputRequestDto);
        });

        Assertions.assertEquals("A quantidade de produto informada para saída é maior que o total em estoque atual.", error.getMessage());

        Mockito.verify(productRepository, times(1)).findByItemCode(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName(value = "Deverá retornar um erro quando tentar realizar a retirada de um item que não o status é diferente de IN_STOCK")
    void mustBeReturnExceptionWhenTryToOutputAItemAndStatusIsNotInStock(){

        ProductOutputRequestDto productOutputRequestDto = ProductOutputRequestDto.builder()
                .code("ADETG31532")
                .quantity(10)
                .build();

        when(productRepository.findByItemCode(Mockito.anyString())).thenReturn(Optional.of(createAProduct(StatusItemEnum.REMOVED_STOCK, 22)));

        Throwable error= Assertions.assertThrows(BusinessException.class, () -> {
            productService.outputProduct(productOutputRequestDto);
        });

        Assertions.assertEquals("Produto indisponível para saída. Favor verificar o status do item no estoque " +
                "e tentar novamente. Apenas itens com status Em estoque podem ser retirados do estoque", error.getMessage());

        Mockito.verify(productRepository, times(1)).findByItemCode(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    private Product createAProduct(StatusItemEnum statusItemEnum, int quantity) {

        return Product.builder()
                .statusProduct(createAStatusProduct(statusItemEnum))
                .createdAt(LocalDateTime.now())
                .itemCode("AAAAA11111")
                .registerUser("admin")
                .product("Notebook")
                .price(BigDecimal.valueOf(10234.23))
                .updatedAt(null)
                .quantity(quantity)
                .build();
    }

    private ProductRequestDto createAProductRequestDto() {
        return ProductRequestDto.builder()
                .quantity(10)
                .price(BigDecimal.valueOf(2000.23))
                .product("Notebook")
                .build();
    }

    private List<Product> createAProductList() {
        return List.of(createAProduct(StatusItemEnum.IN_STOCK, 10));
    }

    private StatusProduct createAStatusProduct(StatusItemEnum statusItemEnum) {
        if(statusItemEnum == null) return null;
        return new StatusProduct(statusItemEnum.getCode(), statusItemEnum.name());
    }
}
