package com.hotsix.iAmNotAlone.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hotsix.iAmNotAlone.domain.dto.AddMemberDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private LocalDate birth;
    private int gender;
    private String introduction;
    private String path;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Personality> personalities = new ArrayList<>();

    public static Member from(AddMemberDto form, Region region) {
        return Member.builder()
            .email(form.getEmail())
            .nickname(form.getNickname())
            .password(form.getPassword())
            .birth(form.getBirth())
            .gender(form.getGender())
            .introduction(form.getIntroduction())
            .path(form.getPath())
            .region(region)
            .personalities(form.getPersonalities())
            .build();
    }

}
