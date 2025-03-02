package com.group.tks_store.product.category.dto;

public class CategoryDetailDTO {
    private Long id;
    private String name;
    private String nameKh;
    private String description;

    public CategoryDetailDTO(Long id, String name, String nameKh, String description) {
        this.id = id;
        this.name = name;
        this.nameKh = nameKh;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKh() {
        return nameKh;
    }

    public void setNameKh(String nameKh) {
        this.nameKh = nameKh;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
