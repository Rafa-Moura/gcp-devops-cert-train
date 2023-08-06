package br.com.stockapi.service;

import br.com.stockapi.infrastructure.enums.StatusItemEnum;
import br.com.stockapi.infrastructure.model.StatusProduct;

public interface StatusProductService {

    StatusProduct getStatusItem(StatusItemEnum statusItemEnum);

}
