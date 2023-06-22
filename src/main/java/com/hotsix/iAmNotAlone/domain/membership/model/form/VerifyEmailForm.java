package com.hotsix.iAmNotAlone.domain.membership.model.form;

import lombok.Getter;

@Getter
public class VerifyEmailForm {

    private String email;
    private String code;
}
