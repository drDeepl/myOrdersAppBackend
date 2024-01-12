package ru.myorder.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import ru.myorder.models.serializers.PurchasedProductSerializer;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="purchased_products")
@JsonSerialize(using= PurchasedProductSerializer.class)
public class PurchasedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name="count", nullable = false)
    private Integer count;

    @ManyToOne
    @JoinColumn(name="measurement_unit_id", nullable = false)
    private MeasurementUnit measurementUnit;

    @Column(name="price", nullable = false)
    private Float price;

    @Column(name="purchase_date",nullable = false)
    private LocalDateTime purchaseData;




}
