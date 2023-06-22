package com.hotsix.iAmNotAlone.domain.main.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MainPostResponse {

    /**
     * 게시글 리스트
     */
    private List<PostProjectionMainDto> postProjectionMainDtoList;
    private Long lastPostId;


    public static MainPostResponse of(List<PostProjectionMainDto> postProjectionMainDtoList,
        Long lastPostId) {
        return MainPostResponse.builder()
            .postProjectionMainDtoList(postProjectionMainDtoList)
            .lastPostId(lastPostId)
            .build();
    }
}
