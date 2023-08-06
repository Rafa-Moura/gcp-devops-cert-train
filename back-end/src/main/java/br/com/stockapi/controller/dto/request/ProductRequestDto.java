package br.com.stockapi.controller.dto.request;

import br.com.stockapi.controller.annotation.PositiveBigDecimal;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDto {

    @NotBlank(message = "é obrigatório informar um produto para inserir")
    private String product;

    @NotNull(message = "é obrigatório informar ao menos 1 item para inserir")
    @Positive
    @Min(1)
    private Integer quantity;

    @NotNull(message = "é obrigatório informar o preço do item para inserir")
    @PositiveBigDecimal
    private BigDecimal price;
}
