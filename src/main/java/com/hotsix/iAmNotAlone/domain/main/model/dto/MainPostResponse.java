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
    private List<PostMainDto> postMainDtoList;
    private Long lastPostId;


    public static MainPostResponse of(List<PostMainDto> postMainDtos, Long lastPostId) {
        return MainPostResponse.builder()
            .postMainDtoList(postMainDtos)
            .lastPostId(lastPostId)
            .build();
    }
}
