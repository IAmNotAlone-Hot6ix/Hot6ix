package com.hotsix.iAmNotAlone.domain.membership.entity;

import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipOAuthForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.ModifyMembershipForm;
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
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

    @Convert(converter = ListToStringConverter.class)
    private List<String> personality;


    public static Membership of(AddMembershipForm form, Region region, String password, String url) {
        return Membership.builder()
                .email(form.getEmail())
                .nickname(form.getNickname())
                .password(password)
                .birth(form.getBirth())
                .gender(form.getGender())
                .introduction(form.getIntroduction())
                .imgPath(url)
                .region(region)
                .personality(form.getPersonality())
                .role(Role.USER)
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    public void updateMembership(ModifyMembershipForm form, Region region) {
        this.nickname = form.getNickname();
        this.introduction = form.getIntroduction();
        this.imgPath = form.getImgPath();
        this.region = region;
        this.personality = form.getPersonality();
    }

    public void updatePassword(String password) {
        this.password = password;
    }


    /**
     * likelist 수정
     */
//    public void updateLikeList(Long postId, boolean isLikeOperation) {
//        this.likes.remove("");
//
//        if (isLikeOperation) {
//            this.likes.add(postId);
//        } else {
//            this.likes.remove(String.valueOf(postId));
//        }
//    }

    public void updateMembership(AddMembershipOAuthForm form, Region region) {
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
            this.personality = form.getPersonality();
        }
        if (form.getGender() != null) {
            this.gender = form.getGender();
        }
    }

}
