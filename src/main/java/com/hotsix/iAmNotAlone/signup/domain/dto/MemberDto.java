package com.hotsix.iAmNotAlone.signup.domain.dto;

import com.hotsix.iAmNotAlone.signup.domain.Member;
import com.hotsix.iAmNotAlone.signup.domain.Region;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {

    private Long id;
    private String email;
    private String nickname;
    private LocalDate birth;
    private String introduction;
    private int gender;
    private String path;
    private Region region;
    private List<String> personalities;

    public static MemberDto from(Member member) {
        return MemberDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .birth(member.getBirth())
            .introduction(member.getIntroduction())
            .gender(member.getGender())
            .path(member.getPath())
            .region(member.getRegion())
            .personalities(member.getPersonalities())
            .build();
    }
}
