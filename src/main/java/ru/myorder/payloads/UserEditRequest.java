package ru.myorder.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserEditRequest {

    private String username;
    private String password;
}
