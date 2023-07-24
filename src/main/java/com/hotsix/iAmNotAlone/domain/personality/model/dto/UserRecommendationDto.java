package com.hotsix.iAmNotAlone.domain.personality.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRecommendationDto {

    private Long memberId;
    private String nickName;
    private String userImage;
    private int age;
    private String region;
    private UserPersonalityDto personality;

}
