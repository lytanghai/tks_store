package com.group.tks_store.product.images.dto;

public class ImageDTO {
    private String id;
    private String uuid;

    public ImageDTO(String id, String uuid) {
        this.id = id;
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
