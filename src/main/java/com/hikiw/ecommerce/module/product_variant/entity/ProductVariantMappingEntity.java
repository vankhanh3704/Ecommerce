package com.hikiw.ecommerce.module.product_variant.entity;


import com.hikiw.ecommerce.module.variant_value.entity.VariantValuesEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "product_variant_mapping")
public class ProductVariantMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    Long mappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_value_id", nullable = false)
    VariantValuesEntity variantValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    ProductVariantEntity productVariant;


    public String getFullInfo(){
        if(variantValue != null && variantValue.getValueName() != null){
            return variantValue.getVariant().getVariantName() + " " + variantValue.getValueName();
        }
        return null;
    }
}
