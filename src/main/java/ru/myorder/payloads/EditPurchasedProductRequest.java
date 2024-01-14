package ru.myorder.payloads;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class EditPurchasedProductRequest {
    private Long productId;
    private Integer count;
    private Long measurementUnitId;
    private Double price;
    private Timestamp purchasedDatetime;

}
