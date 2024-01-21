package ru.myorder.dtos;

import lombok.Data;

@Data
public class PurchasedProductDTO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String productName;
    private Integer count;
    private String unitMeasurement;
    private Float price;
    private String purchaseDate;
}
