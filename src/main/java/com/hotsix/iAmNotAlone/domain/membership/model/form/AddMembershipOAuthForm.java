package com.hotsix.iAmNotAlone.domain.membership.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class AddMembershipOAuthForm {

    private String nickname;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private Integer gender;
    private String introduction;
    private Long regionId;
    private List<String> personality;


}
