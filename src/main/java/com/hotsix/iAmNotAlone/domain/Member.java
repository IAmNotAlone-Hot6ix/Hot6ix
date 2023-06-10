package com.hotsix.iAmNotAlone.domain;

import com.hotsix.iAmNotAlone.domain.dto.AddMemberDto;
import com.hotsix.iAmNotAlone.login.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

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

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public void destroyRefreshToken(){
        this.refreshToken = null;
    }

    //추후삭제 메서드
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
