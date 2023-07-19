package com.hotsix.iAmNotAlone.global.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Profile {

    private String nickname;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
}
