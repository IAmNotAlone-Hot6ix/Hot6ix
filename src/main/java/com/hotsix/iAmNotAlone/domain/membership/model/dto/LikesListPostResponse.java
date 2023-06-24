package com.hotsix.iAmNotAlone.domain.membership.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LikesListPostResponse {

    /**
     * 게시글 리스트
     */
    private List<LikesListPostProjectionDto> likeListPost;
    private Long lastPostId;


    public static LikesListPostResponse of(List<LikesListPostProjectionDto> likesListPostProjectionDtoList,
        Long lastPostId) {
        return LikesListPostResponse.builder()
            .likeListPost(likesListPostProjectionDtoList)
            .lastPostId(lastPostId)
            .build();
    }
}
