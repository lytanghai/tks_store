package com.group.tks_store.product.variant_image.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.product.variant.entity.VariantEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "variant_images")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VariantImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "variant_id", referencedColumnName = "id")
    private VariantEntity variant;

    @Column(name = "image")
    private String image;

    @Column(name = "created_at", updatable = false)
    private Date createdAt = new Date();

    @Column(name = "last_updated_at")
    private Date lastUpdatedAt = new Date();

    public Integer getId() {
        return id;
    }

    public Integer setId(Integer id) {
        return this.id = id;
    }

    public VariantEntity getVariant() {
        return variant;
    }

    public void setVariant(VariantEntity variant) {
        this.variant = variant;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
