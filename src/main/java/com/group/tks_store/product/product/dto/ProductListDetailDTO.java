package com.group.tks_store.product.product.dto;

import com.group.tks_store.product.category.dto.CategoryDetailDTO;
import com.group.tks_store.product.variant.dto.VariantDetailDTO;

import java.util.Date;
import java.util.List;

public class ProductListDetailDTO {
    private Integer id;
    private String nameEn;
    private String nameKh;
    private String code;
    private Double salePrice;
    private String currency;
    private String description;
    private String status;
    private Date createdAt;
    private CategoryDetailDTO category;
    private List<VariantDetailDTO> variants;

    public ProductListDetailDTO(Integer id, String nameEn, String nameKh, String code, Double salePrice, String currency,String description, String status, Date createdAt, CategoryDetailDTO category, List<VariantDetailDTO> variants) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameKh = nameKh;
        this.code = code;
        this.salePrice = salePrice;
        this.currency = currency;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.category = category;
        this.variants = variants;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public CategoryDetailDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDetailDTO category) {
        this.category = category;
    }

    public List<VariantDetailDTO> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantDetailDTO> variants) {
        this.variants = variants;
    }
}
