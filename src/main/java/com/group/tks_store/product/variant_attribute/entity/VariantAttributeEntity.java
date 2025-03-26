package com.group.tks_store.product.variant_attribute.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.group.tks_store.product.attribute.entity.AttributeEntity;
import com.group.tks_store.product.variant.entity.VariantEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "variant_attributes")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VariantAttributeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    @JsonManagedReference
    private AttributeEntity attribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private VariantEntity variant;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "group_num")
    private Integer groupNum;

    @Column(name = "status")
    private String status;

}