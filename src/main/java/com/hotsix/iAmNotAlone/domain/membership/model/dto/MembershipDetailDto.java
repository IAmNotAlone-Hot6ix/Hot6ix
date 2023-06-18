package com.hotsix.iAmNotAlone.domain.membership.model.dto;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MembershipDetailDto {

    private Long id;
    private String email;
    private String nickname;
    private String img_path;
    private int gender;
    private LocalDate birth;
    private Region region;
    private List<String> personality;
    private String introduction;

    public MembershipDetailDto(Membership membership) {
        id = membership.getId();
        email = membership.getEmail();
        nickname = membership.getNickname();
        img_path = membership.getImgPath();
        gender = membership.getGender();
        birth = membership.getBirth();
        region = membership.getRegion();
        personality = membership.getPersonality();
        introduction = membership.getIntroduction();
    }
}
