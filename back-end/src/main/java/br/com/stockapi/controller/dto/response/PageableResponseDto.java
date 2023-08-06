package br.com.stockapi.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageableResponseDto {

    private List<?> content;

    private Integer pageNumber;

    private Integer maxPageContentSize;

    private Integer totalPages;

    private boolean lastPage;

    private boolean firstPage;

}
