package com.hotsix.iAmNotAlone.domain.post.model.dto;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipPostDto;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailDto {

    private Long postId;
    private Long boardId;
    private MembershipPostDto membership;
    private Long region_id;
    private String address;
    private String content;
    private Long likes;
    private int gender;
    private List<String> img_path;

    public static PostDetailDto from(Post post) {
        return PostDetailDto.builder()
            .postId(post.getId())
            .boardId(post.getBoard_id())
            .membership(MembershipPostDto.from(post.getMembership()))
            .region_id(post.getRegion_id())
            .address(post.getAddress())
            .content(post.getContent())
            .likes(post.getLikes())
            .gender(post.getGender())
            .img_path(post.getImg_path())
            .build();
    }
}
