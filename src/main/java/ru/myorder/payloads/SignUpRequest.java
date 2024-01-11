package ru.myorder.payloads;

import lombok.Data;

@Data
public class SignUpRequest {

    private String username;
    private String password;
    private Boolean isAdmin;

}