package com.tatoeba.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Getter
@Setter
public final class BaseEntityPageProduce<T> {
    private final List<T> content;
    private final Integer page;
    private final Integer size;
    @JsonProperty("total_pages")
    private final Integer totalPages;
    @JsonProperty("total_elements")
    private final Long totalElements;

    public static <T> BaseEntityPageProduce<T> build(Page<T> page) {
        return BaseEntityPageProduce
                .<T>builder()
                .page(page.getPageable().getPageNumber())
                .size(page.getPageable().getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent())
                .build();
    }

    private Integer getTotalPages() {
        if (size != null && totalElements != null) {
            return size == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) size);
        }
        return totalPages;
    }
}