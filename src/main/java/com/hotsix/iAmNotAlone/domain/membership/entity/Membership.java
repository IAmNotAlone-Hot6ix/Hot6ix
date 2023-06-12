package com.hotsix.iAmNotAlone.domain.membership.entity;

import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.global.auth.common.Role;
import com.hotsix.iAmNotAlone.global.util.ListToStringConverter;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private Long region_id;

    private String email;
    private String nickname;
    private String password;
    private LocalDate birth;
    private int gender;
    private String introduction;
    private String img_path;

    @Column(name = "likelist", length = 100)
    private String likes;

    @Convert(converter = ListToStringConverter.class)
    @Column(insertable = false, updatable = false)
    private List<String> likelist;


    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Column(name = "personalitylist", length = 100)
    private String personality;

    @Convert(converter = ListToStringConverter.class)
    @Column(insertable = false, updatable = false)
    private List<String> personalitylist;


    public static Membership of(AddMembershipForm form, Region region, String password) {
        ListToStringConverter converter = new ListToStringConverter();

        return Membership.builder()
            .email(form.getEmail())
            .nickname(form.getNickname())
            .password(password)
            .birth(form.getBirth())
            .gender(form.getGender())
            .introduction(form.getIntroduction())
            .img_path(form.getPath())
            .region_id(region.getId())
            .personality(converter.convertToDatabaseColumn(form.getPersonalities()))
            .role(Role.USER)
            .build();
    }


    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

}
