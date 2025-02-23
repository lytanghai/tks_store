//package com.group.tks_store.product.variant_image.entity;
//
//import com.group.tks_store.product.variant.entity.VariantEntity;
//import org.springframework.data.annotation.Id;
//
//import javax.persistence.*;
//import java.util.Date;
//
////@Entity
////@Table(name = "product_variant_images", schema = "tks")
//public class VariantImageEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne
//    @JoinColumn(name = "variant_id", nullable = false)
//    private VariantEntity variant;
//
//    private String image;
//
//    @Column(name = "created_at", updatable = false)
//    private Date createdAt = new Date();
//
//    public Integer getId() {
//        return id;
//    }
//
//    public Integer setId(Integer id) {
//        return this.id = id;
//    }
//
//    public VariantEntity getVariant() {
//        return variant;
//    }
//
//    public void setVariant(VariantEntity variant) {
//        this.variant = variant;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }
//}
