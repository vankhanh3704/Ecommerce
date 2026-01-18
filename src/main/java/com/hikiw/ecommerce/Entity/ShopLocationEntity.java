package com.hikiw.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop_location")
public class ShopLocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_location_id")
    Integer shopLocationId;

    // ========== RELATIONSHIPS ==========

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    ShopEntity shop;

    @OneToMany(mappedBy = "shopLocation", cascade = CascadeType.ALL)
    Set<ProductEntity> products;

    // ========== CONTACT INFO ==========

    @Column(name = "contact_name", length = 100)
    String contactName;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    // ========== ADDRESS DETAILS ==========

    @Column(nullable = false, length = 255)
    String street;

    @Column(length = 100)
    String ward;

    @Column(nullable = false, length = 100)
    String district;

    @Column(nullable = false, length = 100)
    String city;

    @Column(length = 100)
    String country = "Vietnam";

    @Column(name = "postal_code", length = 20)
    String postalCode;

    // ========== BUSINESS FLAGS ==========

    @Column(name = "is_default_pickup")
    Boolean isDefaultPickup = false;

    @Column(name = "is_default_return")
    Boolean isDefaultReturn = false;

    @Column(name = "is_active")
    Boolean isActive = true;

    // ========== ADDITIONAL INFO ==========

    @Column(name = "location_name", length = 100)
    String locationName;

    @Column(columnDefinition = "TEXT")
    String note;


    /**
     * Get full address in readable format
     */
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        address.append(street);
        if (ward != null && !ward.isEmpty()) {
            address.append(", ").append(ward);
        }
        address.append(", ").append(district);
        address.append(", ").append(city);
        if (country != null && !country.isEmpty()) {
            address.append(", ").append(country);
        }
        return address.toString();
    }

}
