package ru.myorder.payloads;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}
