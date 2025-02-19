package com.group.tks_store.product.products.dto;

import com.group.tks_store.product.products.entity.ProductEntity;

import java.util.List;

public class ProductListDTO {
    private Integer totalPages;
    private Integer totalRecords;
    private List<ProductEntity> records;
    private Integer pageNumber;
    private String sortDirection;
    private String sortBy;
    private Boolean first;
    private Boolean last;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<ProductEntity> getRecords() {
        return records;
    }

    public void setRecords(List<ProductEntity> records) {
        this.records = records;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }
}
