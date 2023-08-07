package br.com.stockapi.controller.dto.request;

import br.com.stockapi.controller.annotation.PositiveBigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "ProductRequestDto", description = "Objeto de requisição para inserção de novo produto")
public class ProductRequestDto {

    @NotBlank(message = "é obrigatório informar um produto para inserir")
    @Schema(description = "Nome do produto que será inserido no estoque.", example = "Notebook AlienWare")
    private String product;

    @NotNull(message = "é obrigatório informar ao menos 1 item para inserir")
    @Positive
    @Min(1)
    @Schema(description = "Quantidade do produto que será inserido no estoque.", example = "2")
    private Integer quantity;

    @NotNull(message = "é obrigatório informar o preço do item para inserir")
    @PositiveBigDecimal
    @Schema(description = "Preço do produto que será inserido no estoque.", example = "2000.20")
    private BigDecimal price;
}
