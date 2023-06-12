package com.hotsix.iAmNotAlone.domain.membership.model.form;

import lombok.Getter;

@Getter
public class EmailVerifyForm {

    private String email;
    private String code;
}
