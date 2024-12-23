package com.clothes.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    public PaginationResultDto<T> plus(PaginationResultDto<T> other) {
        List<T> mergedData = new ArrayList<>();
        int thisSize = this.data.size();
        int otherSize = other.getData().size();
        int thisIndex = 0, otherIndex = 0;
        while (thisIndex < thisSize || otherIndex < otherSize) {
            if (thisIndex < thisSize && otherIndex < otherSize) {
                mergedData.add(this.data.get(thisIndex));
                mergedData.add(other.getData().get(otherIndex));
                thisIndex++;
                otherIndex++;
            } else if (thisIndex < thisSize) {
                mergedData.add(this.data.get(thisIndex));
                thisIndex++;
            } else {
                mergedData.add(other.getData().get(otherIndex));
                otherIndex++;
            }
        }
        long totalElements = this.totalElements + other.getTotalElements();
        long totalPages = totalElements % mergedData.size() == 0 ? totalElements / mergedData.size() : totalElements / mergedData.size() + 1;
        return new PaginationResultDto<>(mergedData, this.currentPage, (int)totalPages, totalElements);
    }
}
