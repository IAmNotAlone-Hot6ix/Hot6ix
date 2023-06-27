package com.hotsix.iAmNotAlone.domain.post.model.dto;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipSummaryDto;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailDto {

    private Long postId;
    private Long boardId;
    private MembershipSummaryDto membership;
    private Long regionId;
    private String address;
    private String content;
    private Long likes;
    private boolean like;
    private int gender;
    private LocalDateTime createdAt;
    private List<String> imgPath;

    public static PostDetailDto from(Post post) {
        return PostDetailDto.builder()
            .postId(post.getId())
            .boardId(post.getBoardId())
            .membership(MembershipSummaryDto.from(post.getMembership()))
            .regionId(post.getRegionId())
            .address(post.getAddress())
            .content(post.getContent())
            .likes(post.getLikes())
            .gender(post.getGender())
            .createdAt(post.getCreatedAt())
            .imgPath(post.getImgPath())
            .build();
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public void setLikes(Long likeCount) {
        this.likes = likes + likeCount;
    }
}
