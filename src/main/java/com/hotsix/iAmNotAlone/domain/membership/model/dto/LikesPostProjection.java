package com.hotsix.iAmNotAlone.domain.membership.model.dto;

import java.time.LocalDateTime;

public interface LikesPostProjection {

    Long getBoardId();

    Long getPostId();

    Long getRegionId();

    String getAddress();

    String getContent();

    LocalDateTime getCreatedAt();

    Long getMemberId();

    String getNickName();

    Long getGender();

    String getUserFile();

    int getCommentCount();

    String getRoomFiles();

}
