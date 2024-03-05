package br.com.stockapi.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ProducResponseDto", description = "Objeto de retorno dos dados do produto")
public class ProductResponseDto {
    @Schema(description = "Código do produto.", example = "CJHDI88879")
    private String itemCode;
    @Schema(description = "Nome do produto.", example = "Notebook AlienWare")
    private String product;
    @Schema(description = "Quantidade atual do produto em estoque.", example = "20")
    private Integer quantity;
    @Schema(description = "Preço do produto.", example = "2000.00")
    private BigDecimal price;
    @Schema(description = "Status atual do produto", example = "Em estoque")
    private String status;
}
