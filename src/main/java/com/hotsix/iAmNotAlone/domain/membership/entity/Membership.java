package com.hotsix.iAmNotAlone.domain.membership.entity;

import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.UpdateMembershipForm;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.global.auth.common.Role;
import com.hotsix.iAmNotAlone.global.util.ListToStringConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Membership extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Region region;

    private String email;
    private String nickname;
    private String password;
    private LocalDate birth;
    private int gender;
    private String introduction;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "likelist", length = 100)
    private String likes;

    @Convert(converter = ListToStringConverter.class)
    @Column(insertable = false, updatable = false)
    private List<String> likelist;

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
                .personality(form.getPersonalities())
                .role(Role.USER)
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    public void updateMembership(UpdateMembershipForm form, Region region) {
        this.nickname = form.getNickname();
        this.introduction = form.getIntroduction();
        this.imgPath = form.getPath();
        this.region = region;
        this.personality = form.getPersonalities();
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
