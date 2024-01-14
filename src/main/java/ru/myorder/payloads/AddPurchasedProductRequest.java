package ru.myorder.payloads;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AddPurchasedProductRequest {
    private Long userId;
    private Long productId;
    private Integer count;
    private Long unitMeasurement;
    private Double price;
    private Timestamp purchasedDatetime;
}
