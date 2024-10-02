package com.clothes.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PaginationResultDto<T> {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public PaginationResultDto(List<T> data, int currentPage, int totalPages, long totalElements) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

}
