package com.group.tks_store.product.product.dto;

import java.util.Date;

public class ProductListDTO {
    private Integer id;
    private String nameEn;
    private String nameKh;
    private String code;
    private Double salePrice;
    private String currency;
    private String description;
    private String status;
    private Date createdAt;
    private Date lastUpdatedAt;
    private String categoryNameEn;
    private String categoryNameKh;

    public ProductListDTO(Integer id, String nameEn, String nameKh, String code, Double salePrice, String currency, String description, String status, Date createdAt, Date lastUpdatedAt, String categoryNameEn, String categoryNameKh) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameKh = nameKh;
        this.code = code;
        this.salePrice = salePrice;
        this.currency = currency;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.categoryNameEn = categoryNameEn;
        this.categoryNameKh = categoryNameKh;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameKh() {
        return nameKh;
    }

    public void setNameKh(String nameKh) {
        this.nameKh = nameKh;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getCategoryNameEn() {
        return categoryNameEn;
    }

    public void setCategoryNameEn(String categoryName) {
        this.categoryNameEn = categoryName;
    }

    public String getCategoryNameKh() {
        return categoryNameKh;
    }

    public void setCategoryNameKh(String categoryNameKh) {
        this.categoryNameKh = categoryNameKh;
    }
}
