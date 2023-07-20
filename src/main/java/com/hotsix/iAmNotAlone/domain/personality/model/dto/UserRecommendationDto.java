package com.hotsix.iAmNotAlone.domain.personality.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRecommendationDto {

    private Long member_id;
    private String nickName;
    private String user_image;
    private int age;
    private String region;
    private UserPersonalityDto personality;

}
