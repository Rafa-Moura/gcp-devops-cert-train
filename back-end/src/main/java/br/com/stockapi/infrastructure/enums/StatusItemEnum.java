package br.com.stockapi.infrastructure.enums;

import br.com.stockapi.controller.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum StatusItemEnum {

    IN_STOCK(1L, "Em estoque"),
    OUT_STOCK(2L, "Sem unidade no estoque"),
    REMOVED_STOCK(3L, "Item removido do estoque");

    private Long code;
    private String description;

    public static String getDescriptionByEnumName(String statusEnum) throws BusinessException {
        return switch (statusEnum) {
            case "IN_STOCK" -> IN_STOCK.getDescription();
            case "OUT_STOCK" -> OUT_STOCK.getDescription();
            case "REMOVED_STOCK" -> REMOVED_STOCK.getDescription();
            default -> throw new BusinessException(HttpStatus.BAD_REQUEST.toString(), "Status invalido");
        };
    }
}
