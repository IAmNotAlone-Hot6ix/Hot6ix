package com.hotsix.iAmNotAlone.domain.comments.model.dto;

import com.hotsix.iAmNotAlone.domain.comments.entity.Comments;
import lombok.Getter;

import java.time.LocalDateTime;

//상세페이지 dto
@Getter
public class CommentsDetailResponseDto {

    private String nickName;
    private String img_path;
    private LocalDateTime createdAt;
    private String content;

    public CommentsDetailResponseDto(Comments comments) {
        nickName = comments.getMembership().getNickname();
        img_path = comments.getMembership().getImgPath();
        createdAt = comments.getCreatedAt();
        content = comments.getContent();
    }
}
