package br.com.stockapi.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum StatusItemEnum {

    IN_STOCK(1L, "Em estoque"),
    OUT(2L, "Sem unidade no estoque"),
    REMOVED_STOCK(3L, "Item removido do estoque");

    private Long code;
    private String description;

    public static String getDescriptionByEnumName(String statusEnum) {
        return switch (statusEnum) {
            case "IN_STOCK" -> IN_STOCK.getDescription();
            case "OUT" -> OUT.getDescription();
            case "REMOVED_STOCK" -> REMOVED_STOCK.getDescription();
            default -> throw new RuntimeException("Status invalido");
        };
    }
}
