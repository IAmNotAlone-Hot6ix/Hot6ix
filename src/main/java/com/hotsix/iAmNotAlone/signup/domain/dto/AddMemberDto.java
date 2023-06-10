package com.hotsix.iAmNotAlone.signup.domain.dto;

import com.hotsix.iAmNotAlone.signup.domain.Personality;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddMemberDto {

    private String email;
    private String nickname;
    private String password;
    private LocalDate birth;
    private int gender;
    private String introduction;
    private String path;
    private Long regionId;
    private List<Personality> personalities;

}
