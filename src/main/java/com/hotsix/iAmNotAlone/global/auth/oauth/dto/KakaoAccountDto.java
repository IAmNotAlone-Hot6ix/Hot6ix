package com.hotsix.iAmNotAlone.global.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoAccountDto {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;
}
