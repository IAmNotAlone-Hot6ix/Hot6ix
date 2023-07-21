package com.hotsix.iAmNotAlone.domain.personality.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPersonalityDto {

    private Long userPersonalityId;
    private String mbti;
    private int smoking;
    private int activeTime;
    private int pets;

}
