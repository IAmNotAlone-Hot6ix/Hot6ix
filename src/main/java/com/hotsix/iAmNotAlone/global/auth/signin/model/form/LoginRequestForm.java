package com.hotsix.iAmNotAlone.global.auth.signin.model.form;

import lombok.Data;

@Data
public class LoginRequestForm {
    private String email;
    private String password;
}
