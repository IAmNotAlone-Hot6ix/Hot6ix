package com.hotsix.iAmNotAlone.domain.personality.entity;

import com.hotsix.iAmNotAlone.domain.personality.model.form.PersonalityDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Personality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personality_id")
    private Long id;

    /**
     * 개인 성향
     */
    private String mbti;
    private int smoking;
    @Column(name = "active_time")
    private int activeTime;
    private int pets;

    /**
     * 개인 선호 성향
     */
    @Column(name = "prefer_smoking")
    private int preferSmoking;
    @Column(name = "prefer_active_time")
    private int preferActiveTime;
    @Column(name = "prefer_pets")
    private int preferPets;
    @Column(name = "prefer_age")
    private int preferAge;


    public static Personality from(PersonalityDto personalityDto) {
        return Personality.builder()
            .mbti(personalityDto.getMbti())
            .smoking(personalityDto.getSmoking())
            .activeTime(personalityDto.getActiveTime())
            .pets(personalityDto.getPets())
            .preferSmoking(personalityDto.getPreferSmoking())
            .preferActiveTime(personalityDto.getPreferActiveTime())
            .preferPets(personalityDto.getPreferPets())
            .preferAge(personalityDto.getPreferAge())
            .build();
    }

    public void updateFromDto(PersonalityDto personalityDto) {
        this.mbti = personalityDto.getMbti();
        this.smoking = personalityDto.getSmoking();
        this.activeTime = personalityDto.getActiveTime();
        this.pets = personalityDto.getPets();
        this.preferSmoking = personalityDto.getPreferSmoking();
        this.preferActiveTime = personalityDto.getPreferActiveTime();
        this.preferPets = personalityDto.getPreferPets();
        this.preferAge = personalityDto.getPreferAge();
    }

}
