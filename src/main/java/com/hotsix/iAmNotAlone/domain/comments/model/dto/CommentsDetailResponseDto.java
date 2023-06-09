package com.hotsix.iAmNotAlone.domain.comments.model.dto;

import com.hotsix.iAmNotAlone.domain.comments.entity.Comments;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//상세페이지 dto
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDetailResponseDto {

    private String nickName;
    private String imgPath;
    private String createdAt;
    private String content;
    private Long commentId;
    private Long memberId;

    public CommentsDetailResponseDto(Comments comments) {
        nickName = comments.getMembership().getNickname();
        imgPath = comments.getMembership().getImgPath();
        createdAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .format(comments.getCreatedAt());
        content = comments.getContent();
        commentId = comments.getId();
        memberId = comments.getMembership().getId();
    }
}
