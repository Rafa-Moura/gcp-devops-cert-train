package br.com.stockapi.controller;

import br.com.stockapi.controller.dto.request.ProductRequestDto;
import br.com.stockapi.controller.dto.response.PageableResponseDto;
import br.com.stockapi.controller.dto.response.ProductResponseDto;
import br.com.stockapi.controller.exception.InvalidRequestException;
import br.com.stockapi.controller.exception.NotFoundException;
import br.com.stockapi.controller.exception.SystemException;
import br.com.stockapi.infrastructure.enums.StatusItemEnum;
import br.com.stockapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/product")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "stock-api")
public class ProductController {

    private final ProductService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Realiza inserção de um novo produto no banco de dados do estoque", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inserção de produto realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na solicitação, verifique os parâmetros informados e tente novamente.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvalidRequestException.class))}
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do sistema. Tente novamente em alguns instantes ou contate um administrador.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SystemException.class))}
            ),
    })
    public void insertItem(@RequestBody @Valid ProductRequestDto productRequestDto) {

        log.info("Iniciando controller de inserção de novo item no estoque. Item: [{}]", productRequestDto.getProduct());

        service.insertItem(productRequestDto);

        log.info("Finalizando controller de inserção de novo item no estoque. Item: [{}]", productRequestDto.getProduct());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retorna os produtos inseridos banco de dados do estoque. A operação suporta paginação e filtro por status", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na solicitação, verifique os parâmetros informados e tente novamente.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvalidRequestException.class))}
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do sistema. Tente novamente em alguns instantes ou contate um administrador.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SystemException.class))}
            ),
    })
    @PageableAsQueryParam
    public ResponseEntity<PageableResponseDto> getAllProducts(@ParameterObject Pageable pageable,
                                                              @Parameter(name = "status", schema = @Schema(implementation = StatusItemEnum.class))
                                                              @PathParam(value = "status") StatusItemEnum status) {

        log.info("Iniciando controller de listagem de produtos");

        PageableResponseDto pageableResponseDto = service.getAllProducts(pageable, status);

        log.info("Finalizando controller de listagem de produtos");

        return new ResponseEntity<>(pageableResponseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retorna os dados do produto do banco de dados do estoque.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na solicitação, verifique os parâmetros informados e tente novamente.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvalidRequestException.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado com os valores fornecidos, verifique os parâmetros informados e tente novamente.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotFoundException.class))}
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do sistema. Tente novamente em alguns instantes ou contate um administrador.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SystemException.class))}
            ),
    })
    public ResponseEntity<ProductResponseDto> getProductByCode(@PathVariable String code) throws NotFoundException {
        log.info("Iniciando controller de visualizar detalhes de um produto. Código do produto: [{}]", code);

        ProductResponseDto productResponseDto = service.getProdutcByCode(code);

        log.info("Finalizando controller de visualizar detalhes de um produto. Código do produto: [{}]", code);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);

    }

    @DeleteMapping(value = "/{code}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Realiza a exclusão do produto do banco de dados do estoque.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na solicitação, verifique os parâmetros informados e tente novamente.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = InvalidRequestException.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado com os valores fornecidos, verifique os parâmetros informados e tente novamente.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = NotFoundException.class))}
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno do sistema. Tente novamente em alguns instantes ou contate um administrador.",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SystemException.class))}
            ),
    })
    public void deleteProduct(@PathVariable String code) throws NotFoundException {
        log.info("Iniciando controller de visualizar detalhes de um produto. Código do produto: [{}]", code);

        service.deleteProduct(code);

        log.info("Finalizando controller de visualizar detalhes de um produto. Código do produto: [{}]", code);
    }
}
