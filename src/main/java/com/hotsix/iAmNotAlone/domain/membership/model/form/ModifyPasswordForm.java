package com.hotsix.iAmNotAlone.domain.membership.model.form;

import lombok.Getter;

@Getter
public class ModifyPasswordForm {

    private String password;
    private String newPassword;
    private String newPasswordVerify;
}
