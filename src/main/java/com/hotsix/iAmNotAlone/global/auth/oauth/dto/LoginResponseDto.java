package com.hotsix.iAmNotAlone.global.auth.oauth.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    public boolean firstLogin;
    private String accessToken;
    private String refreshToken;
}
