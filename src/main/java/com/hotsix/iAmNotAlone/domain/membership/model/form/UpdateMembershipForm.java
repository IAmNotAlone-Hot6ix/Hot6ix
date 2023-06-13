package com.hotsix.iAmNotAlone.domain.membership.model.form;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateMembershipForm {

    private String nickname;
    private String introduction;
    private String path;
    private Long regionId;
    private List<String> personalities;
}
