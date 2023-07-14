package com.hotsix.iAmNotAlone.global.auth.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
}
