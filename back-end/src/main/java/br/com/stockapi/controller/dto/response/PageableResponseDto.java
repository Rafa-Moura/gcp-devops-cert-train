package br.com.stockapi.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PageableResponseDto", description = "Objeto de retorno dos dados paginados dos produtos")
public class PageableResponseDto {

    @Schema(description = "Lista com o conteúdo retornado do banco de dados dos produtos.")
    private List<ProductResponseDto> content;

    @Schema(description = "Número da página atual. A contagem das páginas começa em 0.", example = "0")
    private Integer pageNumber;

    @Schema(description = "Quantidade dos produtos exibidos por página, valor default é 20.", example = "20")
    private Integer maxPageContentSize;

    @Schema(description = "Total de produtos retornados na requisição. Mostra o total de itens no banco de dados com base no filtro.", example = "30")
    private Integer totalElements;

    @Schema(description = "Total de páginas para navegação.", example = "2")
    private Integer totalPages;

    @Schema(description = "Indica se a página atual do usuário é a última página.", example = "false")
    private boolean lastPage;

    @Schema(description = "Indica se a página atual do usuário é a primeira página.", example = "true")
    private boolean firstPage;

}
