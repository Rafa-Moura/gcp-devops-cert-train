package br.com.stockapi.service.impl;

import br.com.stockapi.controller.exception.NotFoundException;
import br.com.stockapi.infrastructure.enums.StatusItemEnum;
import br.com.stockapi.infrastructure.model.StatusProduct;
import br.com.stockapi.infrastructure.repository.StatusProductRepository;
import br.com.stockapi.service.StatusProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatusProductServiceImpl implements StatusProductService {

    private final StatusProductRepository statusProductRepository;

    @Override
    public StatusProduct getStatusItem(StatusItemEnum statusItemEnum) throws NotFoundException {

        if (statusItemEnum == null) return null;

        return statusProductRepository.findById(statusItemEnum.getCode()).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.toString(), "Status inválido"));

    }
}
