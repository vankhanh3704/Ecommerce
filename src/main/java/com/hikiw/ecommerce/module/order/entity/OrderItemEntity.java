package com.hikiw.ecommerce.module.order.entity;


import com.hikiw.ecommerce.module.product_variant.entity.ProductVariantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "order_item")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    ProductVariantEntity productVariant;

    // Snapshot giá tại thời điểm đặt hàng (giá có thể thay đổi sau)
    // phải lưu các thuộc tính mà mình đã đặt mua hàng tại thời điểm đó ( vì nó có thể bị thay đổi sau khi mình đặt hang)
    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(name = "sku", nullable = false)
    String sku;

    @Column(name = "variant_info")
    String variantInfo;  // VD: "Đỏ - M"

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "price_per_unit", nullable = false)
    Double pricePerUnit;  // Giá tại thời điểm mua

    @Column(nullable = false)
    Integer quantity;

    @Column(name = "total_price", nullable = false)
    Double totalPrice;  // pricePerUnit * quantity

    // ========== HELPER ==========
    public Double calculateTotal() {
        return pricePerUnit * quantity;
    }
}
