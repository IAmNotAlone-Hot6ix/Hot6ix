package com.hotsix.iAmNotAlone.domain.membership.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.hotsix.iAmNotAlone.domain.personality.model.form.PersonalityDto;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddMembershipForm {

    private String email;
    private String nickname;
    private String password;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private int gender;
    private String introduction;
    private Long regionId;
    private PersonalityDto personality;
    private boolean verify;

}
