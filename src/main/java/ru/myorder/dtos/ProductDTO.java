package ru.myorder.dtos;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
}
