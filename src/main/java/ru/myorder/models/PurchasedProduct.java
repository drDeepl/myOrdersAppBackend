package ru.myorder.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.myorder.models.serializers.PurchasedProductSerializer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
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
    private Double price;

    @Column(name="purchase_date",nullable = false)
    private Timestamp purchaseDate;

    public PurchasedProduct(User user, Product product, Integer count, Double price, MeasurementUnit measurementUnit, Timestamp purchaseDate){
        this.user = user;
        this.product = product;
        this.count = count;
        this.price = price;
        this.measurementUnit = measurementUnit;
        this.purchaseDate = purchaseDate;
    }




}
