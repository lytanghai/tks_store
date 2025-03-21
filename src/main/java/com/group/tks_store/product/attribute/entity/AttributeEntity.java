package com.group.tks_store.product.attribute.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.product.variant_attribute.entity.VariantAttributeEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "attributes")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AttributeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_kh")
    @JsonProperty("name_kh")
    private String nameKh;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<VariantAttributeEntity> variantAttributes;

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

    public List<VariantAttributeEntity> getVariantAttributes() {
        return variantAttributes;
    }

    public void setVariantAttributes(List<VariantAttributeEntity> variantAttributes) {
        this.variantAttributes = variantAttributes;
    }
}
