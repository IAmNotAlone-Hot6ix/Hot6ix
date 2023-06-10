package com.hotsix.iAmNotAlone.signup.domain.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateMemberDto {

    private String introduction;
    private String path;
    private Long regionId;
    private List<String> personalities;
}
