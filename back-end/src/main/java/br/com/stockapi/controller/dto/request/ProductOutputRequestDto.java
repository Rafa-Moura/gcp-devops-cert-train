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
@Schema(name = "ProductOutputRequestDto", description = "Objeto de requisição para saída produto")
public class ProductOutputRequestDto {

    @NotBlank(message = "é obrigatório informar um produto para registrar a saída")
    @Schema(description = "Código do produto.", example = "ABESDQ21549")
    private String code;

    @NotNull(message = "é obrigatório informar ao menos 1 item para registrar a saída")
    @Positive
    @Min(value = 1, message = "informar ao menos 1 produto para saída")
    @Schema(description = "Quantidade do produto.", example = "2")
    private Integer quantity;
}
