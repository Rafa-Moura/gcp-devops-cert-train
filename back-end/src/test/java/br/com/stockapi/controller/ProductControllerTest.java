package br.com.stockapi.controller;

import br.com.stockapi.controller.dto.request.ProductOutputRequestDto;
import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.dto.response.PageableResponseDto;
import br.com.stockapi.controller.dto.response.ProductResponseDto;
import br.com.stockapi.controller.exception.NotFoundException;
import br.com.stockapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService productService;
    private final String URL_BASE = "http://localhost:8080/api/product";
    PageableResponseDto pageableResponseDto;
    ProductResponseDto productResponseDto;
    ProductRequestDto productRequestDto;
    ProductOutputRequestDto productOutputRequestDto;
    @BeforeEach
    void setUp() {
        productOutputRequestDto = ProductOutputRequestDto.builder()
                .code("AAAAA11111")
                .quantity(10)
                .build();

        productRequestDto = ProductRequestDto.builder()
                .product("Notebook Dell Latitude 3451")
                .price(BigDecimal.valueOf(3049.44))
                .quantity(23)
                .build();

        productResponseDto = ProductResponseDto.builder()
                .quantity(10)
                .itemCode("AAASD93847")
                .price(BigDecimal.valueOf(23.44))
                .status("Em estoque")
                .product("Cabo USB tipo C")
                .build();

        pageableResponseDto = PageableResponseDto.builder()
                .content(List.of(ProductResponseDto.builder()
                        .quantity(10)
                        .itemCode("AAASD93847")
                        .price(BigDecimal.valueOf(23.44))
                        .status("Em estoque")
                        .product("Cabo USB tipo C")
                        .build()))
                .totalPages(1)
                .firstPage(true)
                .lastPage(true)
                .maxPageContentSize(10)
                .pageNumber(0)
                .totalElements(1)
                .build();
    }

    @Test
    @DisplayName(value = "Deverá retornar uma lista de produtos cadastrados no sistema e status code 200")
    void mustBeReturnProductListAndStatusCode200() throws Exception {

        when(productService.getAllProducts(Mockito.any(), Mockito.any())).thenReturn(pageableResponseDto);

        mockMvc.perform(get(URL_BASE)
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].itemCode").value(pageableResponseDto.getContent().get(0).getItemCode()))
                .andExpect(jsonPath("$.content[0].quantity").value(pageableResponseDto.getContent().get(0).getQuantity()))
                .andExpect(jsonPath("$.content[0].price").value(pageableResponseDto.getContent().get(0).getPrice()))
                .andExpect(jsonPath("$.content[0].status").value(pageableResponseDto.getContent().get(0).getStatus()))
                .andExpect(jsonPath("$.content[0].product").value(pageableResponseDto.getContent().get(0).getProduct()))
                .andDo(print());

        Mockito.verify(productService, times(1)).getAllProducts(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName(value = "Deverá retornar um único produto cadastrado no sistema e status code 200")
    void mustBeReturnAProductResponseDtoAndStatusCode200() throws Exception {

        when(productService.getProductByCode(Mockito.anyString())).thenReturn(productResponseDto);

        mockMvc.perform(get(URL_BASE + "/AAASD93847")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCode").value(productResponseDto.getItemCode()))
                .andExpect(jsonPath("$.quantity").value(productResponseDto.getQuantity()))
                .andExpect(jsonPath("$.price").value(productResponseDto.getPrice()))
                .andExpect(jsonPath("$.status").value(productResponseDto.getStatus()))
                .andExpect(jsonPath("$.product").value(productResponseDto.getProduct()))
                .andDo(print());

        Mockito.verify(productService, times(1)).getProductByCode(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deverá retornar um erro ao tentar localizar um produto não existente no banco de dados com status code 404")
    void mustBeReturnANotFoundExceptionAndStatusCode404() throws Exception {

        when(productService.getProductByCode(Mockito.anyString())).thenThrow(new NotFoundException(HttpStatus.NOT_FOUND.toString(),
                "Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente."));

        mockMvc.perform(get(URL_BASE + "/AAASD93847")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value(HttpStatus.NOT_FOUND.toString()))
                .andExpect(jsonPath("$.message").value("Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente."))
                .andDo(print());

        Mockito.verify(productService, times(1)).getProductByCode(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deverá retornar um erro ao tentar localizar um produto e receber um erro interno com status code 500")
    void mustBeReturnASystemExceptionAndStatusCode500() throws Exception {

        when(productService.getProductByCode(Mockito.anyString()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get(URL_BASE + "/AAASD93847")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.reason").value(HttpStatus.INTERNAL_SERVER_ERROR.toString()))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro interno, tente novamente mais tarde ou contate um administrador"))
                .andDo(print());

        Mockito.verify(productService, times(1)).getProductByCode(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deverá realizar a inserção de um novo produto no banco de dados e status code 201")
    void mustBeInsertNewProductAndReturnStatusCode201() throws Exception {

        doNothing().when(productService).insertItem(any(ProductRequestDto.class));

        mockMvc.perform(post(URL_BASE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(productRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
        ).andExpect(status().isCreated());

        verify(productService, times(1)).insertItem(any(ProductRequestDto.class));
    }

    @Test
    @DisplayName(value = "Deverá receber uma exceção de InvalidRequestException e status code 400 quando body tiver dados obrigatórios vazios")
    void mustBeReturnInvalidRequestExceptionAndStatusCode400() throws Exception {

        ProductRequestDto errorObject = ProductRequestDto.builder().build();

        mockMvc.perform(post(URL_BASE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(errorObject))
                        .characterEncoding(StandardCharsets.UTF_8)
                ).andExpect(status().isBadRequest()
                ).andExpect(jsonPath("$.reason").value(HttpStatus.BAD_REQUEST.toString()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName(value = "Deverá remover um único produto cadastrado no sistema e retornar status code 204")
    void mustBeDeleteAndStatusCode204() throws Exception {

        doNothing().when(productService).deleteProduct(Mockito.anyString());

        mockMvc.perform(delete(URL_BASE + "/AAASD93847")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andDo(print());

        Mockito.verify(productService, times(1)).deleteProduct(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deverá retornar um erro ao tentar remover um produto não existente no banco de dados com status code 404")
    void mustBeReturnANotFoundExceptionAndStatusCode404WhenDeleteProduct() throws Exception {

        doThrow(new NotFoundException(HttpStatus.NOT_FOUND.toString(),
                "Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente."))
                .when(productService).deleteProduct(Mockito.anyString());

        mockMvc.perform(delete(URL_BASE + "/AAASD93847")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").value(HttpStatus.NOT_FOUND.toString()))
                .andExpect(jsonPath("$.message").value("Ops, não localizamos um produto com o código informado. Verifique o código e tente novamente."))
                .andDo(print());

        Mockito.verify(productService, times(1)).deleteProduct(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deverá retornar um erro ao remover um produto e receber um erro interno com status code 500")
    void mustBeReturnASystemExceptionAndStatusCode500WhenDeleteProduct() throws Exception {

        doThrow(new RuntimeException()).when(productService).deleteProduct(Mockito.anyString());


        mockMvc.perform(delete(URL_BASE + "/AAASD93847")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.reason").value(HttpStatus.INTERNAL_SERVER_ERROR.toString()))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro interno, tente novamente mais tarde ou contate um administrador"))
                .andDo(print());

        Mockito.verify(productService, times(1)).deleteProduct(Mockito.anyString());
    }

    @Test
    @DisplayName(value = "Deverá registrar a saída de um produto com sucesso e retornar o status code 201")
    void mustBeOutputItemSuccessAndReturnStatusCode201() throws Exception {

        ProductResponseDto responseDto = ProductResponseDto.builder()
                .itemCode("AAAAA11111")
                .product("Notebook Dell Latitude 3452")
                .status("Em estoque")
                .price(BigDecimal.valueOf(2001.23))
                .quantity(4)
                .build();

        when(productService.outputProduct(Mockito.any())).thenReturn(responseDto);

        mockMvc.perform(post(URL_BASE + "/output")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(productOutputRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemCode").value(responseDto.getItemCode()))
                .andExpect(jsonPath("$.quantity").value(responseDto.getQuantity()))
                .andExpect(jsonPath("$.price").value(responseDto.getPrice()))
                .andExpect(jsonPath("$.status").value(responseDto.getStatus()))
                .andExpect(jsonPath("$.product").value(responseDto.getProduct()))
                .andDo(print());
    }

}
