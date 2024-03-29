package com.hotsix.iAmNotAlone.domain.membership.model.dto;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.personality.entity.Personality;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import java.time.LocalDate;
import lombok.Data;

@Data
public class MembershipDetailDto {

    private Long id;
    private String email;
    private String nickname;
    private String imgPath;
    private int gender;
    private LocalDate birth;
    private Region region;
    private Personality personality;
    private String introduction;

    public MembershipDetailDto(Membership membership) {
        id = membership.getId();
        email = membership.getEmail();
        nickname = membership.getNickname();
        imgPath = membership.getImgPath();
        gender = membership.getGender();
        birth = membership.getBirth();
        region = membership.getRegion();
        personality = membership.getPersonality();
        introduction = membership.getIntroduction();
    }
}
