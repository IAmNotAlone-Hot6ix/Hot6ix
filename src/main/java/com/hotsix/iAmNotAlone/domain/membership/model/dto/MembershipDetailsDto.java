package com.hotsix.iAmNotAlone.domain.membership.model.dto;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MembershipDetailsDto {

    private Long id;
    private String email;
    private String nickname;
    private LocalDate birth;
    private String introduction;
    private int gender;
    private String path;
    private Region region;
    private List<String> personality;

    public static MembershipDetailsDto from(Membership member) {
        return MembershipDetailsDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .birth(member.getBirth())
            .introduction(member.getIntroduction())
            .gender(member.getGender())
            .path(member.getImgPath())
            .region(member.getRegion())
            .personality(member.getPersonality())
            .build();
    }
}
