package com.hotsix.iAmNotAlone.domain.membership.model.dto;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
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

    public static MemberDto from(Membership member) {
        return MemberDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .birth(member.getBirth())
            .introduction(member.getIntroduction())
            .gender(member.getGender())
            .path(member.getImg_path())
            .region(member.getRegion())
            .personalities(member.getPersonality())
            .build();
    }
}
