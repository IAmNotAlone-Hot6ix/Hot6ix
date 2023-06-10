package com.hotsix.iAmNotAlone.signup.domain.dto;

import lombok.Getter;

@Getter
public class EmailVerifyDto {

    private String email;
    private String code;
}
