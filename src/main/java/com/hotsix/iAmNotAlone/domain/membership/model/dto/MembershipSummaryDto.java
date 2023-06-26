package com.hotsix.iAmNotAlone.domain.membership.model.dto;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MembershipSummaryDto {

    private Long membershipId;
    private String imgPath;
    private String nickname;

    public static MembershipSummaryDto from(Membership membership) {
        return MembershipSummaryDto.builder()
            .membershipId(membership.getId())
            .imgPath(membership.getImgPath())
            .nickname(membership.getNickname())
//            .address(membership.getRegion().getSido() + " " + membership.getRegion().getSigg())
//            .gender(membership.getGender())
            .build();
    }
}
