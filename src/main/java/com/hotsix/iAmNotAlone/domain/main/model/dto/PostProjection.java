package com.hotsix.iAmNotAlone.domain.main.model.dto;

import java.time.LocalDateTime;

public interface PostProjection {

    Long getBoardId();

    Long getPostId();

    Long getRegionId();

    String getAddress();

    String getContent();

    LocalDateTime getCreatedAt();

    Long getMemberId();

    String getNickName();

    int getGender();

    String getUserFile();

    int getCommentCount();

    String getRoomFiles();

}
