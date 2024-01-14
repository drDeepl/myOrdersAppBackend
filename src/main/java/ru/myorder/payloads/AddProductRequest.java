package ru.myorder.payloads;


import lombok.Data;

@Data
public class AddProductRequest {
    private String name;
    private Long categoryId;
}
