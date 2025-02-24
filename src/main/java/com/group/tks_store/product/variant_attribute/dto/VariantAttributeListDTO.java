package com.group.tks_store.product.variant_attribute.dto;

import com.group.tks_store.product.variant_attribute.entity.VariantAttributeEntity;

import java.util.List;

public class VariantAttributeListDTO {
    private List<VariantAttributeEntity> records;
    private int totalPages;
    private long totalRecords;
    private boolean first;
    private boolean last;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;

    public List<VariantAttributeEntity> getRecords() {
        return records;
    }

    public void setRecords(List<VariantAttributeEntity> records) {
        this.records = records;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public boolean getFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean getLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}