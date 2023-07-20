package com.hotsix.iAmNotAlone.domain.membership.entity;

import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipOAuthForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.ModifyMembershipForm;
import com.hotsix.iAmNotAlone.domain.personality.entity.Personality;
import com.hotsix.iAmNotAlone.domain.personality.model.form.PersonalityDto;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.global.auth.common.Role;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Membership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    private String email;
    private String nickname;
    private String password;
    private LocalDate birth;
    private int gender;
    private String introduction;

    @Column(name = "img_path")
    private String imgPath;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "personality_id")
    private Personality personality;
    @Transient
    private PersonalityDto personalityDto;


    public static Membership of(AddMembershipForm form, Region region, String password, String url,
        Personality personality) {
        return Membership.builder()
            .email(form.getEmail())
            .nickname(form.getNickname())
            .password(password)
            .birth(form.getBirth())
            .gender(form.getGender())
            .introduction(form.getIntroduction())
            .imgPath(url)
            .region(region)
            .personality(personality)
            .role(Role.USER)
            .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    public void updateMembership(ModifyMembershipForm form, Region region,
        Personality personality) {
        this.nickname = form.getNickname();
        this.introduction = form.getIntroduction();
        this.imgPath = form.getImgPath();
        this.region = region;
        this.personality = personality;
    }

    public void updatePassword(String password) {
        this.password = password;
    }


    public void updateMembershipAuth(AddMembershipOAuthForm form, Region region,
        Personality personality) {
        if (form.getNickname() != null) {
            this.nickname = form.getNickname();
        }
        if (form.getIntroduction() != null) {
            this.introduction = form.getIntroduction();
        }
        if (form.getBirth() != null) {
            this.birth = form.getBirth();
        }
        if (region != null) {
            this.region = region;
        }
        if (form.getPersonality() != null) {
            this.personality = personality;
        }
        if (form.getGender() != null) {
            this.gender = form.getGender();
        }
    }

}
