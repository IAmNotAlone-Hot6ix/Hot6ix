package com.hotsix.iAmNotAlone.domain.membership.model.form;

import com.hotsix.iAmNotAlone.domain.personality.model.form.PersonalityDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ModifyMembershipForm {

    private String nickname;
    private String introduction;
    private String imgPath;
    private Long regionId;
    private PersonalityDto personality;
}
