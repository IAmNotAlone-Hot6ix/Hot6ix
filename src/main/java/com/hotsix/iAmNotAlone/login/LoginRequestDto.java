package com.hotsix.iAmNotAlone.login;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
