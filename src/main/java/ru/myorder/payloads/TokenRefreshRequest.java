package ru.myorder.payloads;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}