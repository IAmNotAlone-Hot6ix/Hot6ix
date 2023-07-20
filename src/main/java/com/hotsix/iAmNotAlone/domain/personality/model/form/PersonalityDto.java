package com.hotsix.iAmNotAlone.domain.personality.model.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PersonalityDto {

    private String mbti;
    private int smoking;
    private int activeTime;
    private int pets;
    private int preferSmoking;
    private int preferActiveTime;
    private int preferPets;
    private int preferAge;
}
