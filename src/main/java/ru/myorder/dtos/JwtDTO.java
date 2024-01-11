package ru.myorder.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class JwtDTO {

    private String type = "Bearer";
    private String accessToken;
    private String refreshToken;

    public JwtDTO(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


}