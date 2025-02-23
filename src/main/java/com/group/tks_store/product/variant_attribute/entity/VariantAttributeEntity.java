//package com.group.tks_store.product.variant_attribute.entity;
//
//import com.group.tks_store.product.attribute.entity.AttributeEntity;
//import com.group.tks_store.product.variant.entity.VariantEntity;
//import org.springframework.data.annotation.Id;
//
//import javax.persistence.*;
//
////@Entity
////@Table(name = "variant_attributes", schema = "tks")
//public class VariantAttributeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne
//    @JoinColumn(name = "variant_id", nullable = false)
//    private VariantEntity variant;
//
//    @ManyToOne
//    @JoinColumn(name = "attribute_id", nullable = false)
//    private AttributeEntity attribute;
//
//    @Column(nullable = false)
//    private String value;
//    private String status;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
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
//    public AttributeEntity getAttribute() {
//        return attribute;
//    }
//
//    public void setAttribute(AttributeEntity attribute) {
//        this.attribute = attribute;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}
