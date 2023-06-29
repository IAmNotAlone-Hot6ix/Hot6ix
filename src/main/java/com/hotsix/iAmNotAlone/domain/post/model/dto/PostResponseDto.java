package com.hotsix.iAmNotAlone.domain.post.model.dto;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponseDto {

    private String nickname;
    private String imgPath;
    private int gender;
    private LocalDateTime createdAt;
    private String content;
    private Long commentCount;
    private Long postId;
    private String postImgPath;
    private boolean like;

    public static PostResponseDto of(Post post, Long commentCount, boolean likeFlag) {

        return PostResponseDto.builder()
                .nickname(post.getMembership().getNickname())
                .imgPath(post.getMembership().getImgPath())
                .gender(post.getMembership().getGender())
                .createdAt(post.getCreatedAt())
                .content(removeContent(post.getContent()))
                .commentCount(commentCount)
                .postId(post.getId())
                .postImgPath(post.getImgPath().get(0))
                .like(likeFlag)
                .build();

    }

    public static String removeContent(String content) {
        if (content.length() > 30) {
            content = content.substring(0, 30);
            content += "...더보기";
        }
        return content;
    }


}
