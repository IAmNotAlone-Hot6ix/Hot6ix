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


    public static MainPostResponse from(List<PostProjectionMainDto> postProjectionMainDtoList) {
        return MainPostResponse.builder()
            .postProjectionMainDtoList(postProjectionMainDtoList)
            .build();
    }
}
