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
    private List<LikesListDto> likeListPost;
    private Long lastPostId;


    public static LikesListPostResponse of(List<LikesListDto> likesListDtos, Long lastPostId) {
        return LikesListPostResponse.builder()
            .likeListPost(likesListDtos)
            .lastPostId(lastPostId)
            .build();
    }
}
