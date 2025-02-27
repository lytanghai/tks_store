package com.group.tks_store.product.attribute.dto;

public class AttributeDTO {
    private Integer id;
    private String name;
    private String nameKh;
    private String status;

    public AttributeDTO() {
    }

    public AttributeDTO(Integer id, String name, String nameKh, String status) {
        this.id = id;
        this.name = name;
        this.nameKh = nameKh;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}