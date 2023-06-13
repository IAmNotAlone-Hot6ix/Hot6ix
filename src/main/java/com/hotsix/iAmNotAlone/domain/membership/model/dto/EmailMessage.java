package com.hotsix.iAmNotAlone.domain.membership.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailMessage {

    private String to;
    private String subject;
}
