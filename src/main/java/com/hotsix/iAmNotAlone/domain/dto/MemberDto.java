package com.hotsix.iAmNotAlone.domain.dto;

import com.hotsix.iAmNotAlone.domain.Member;
import com.hotsix.iAmNotAlone.domain.Personality;
import com.hotsix.iAmNotAlone.domain.Region;
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
    private Long regionId;
    private Region region;
    private List<Personality> personalities;

    public static MemberDto from(Member member) {
        return MemberDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .birth(member.getBirth())
            .introduction(member.getIntroduction())
            .gender(member.getGender())
            .regionId(member.getRegion().getId())
            .region(member.getRegion())
            .personalities(member.getPersonalities())
            .build();
    }
}
