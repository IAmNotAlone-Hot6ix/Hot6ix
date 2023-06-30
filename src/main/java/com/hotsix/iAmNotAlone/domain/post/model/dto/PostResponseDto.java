package com.hotsix.iAmNotAlone.domain.post.model.dto;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

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
    private String address;
    private boolean like;

    public static PostResponseDto of(Post post, Long commentCount, boolean likeFlag) {
        return PostResponseDto.builder()
                .nickname(post.getMembership().getNickname())
                .imgPath(post.getMembership().getImgPath())
                .gender(post.getMembership().getGender())
                .createdAt(post.getCreatedAt())
                .content(post.getContent())
                .commentCount(commentCount)
                .postId(post.getId())
                .postImgPath(post.getImgPath().get(0))
                .address(post.getAddress())
                .like(likeFlag)
                .build();
    }
}
