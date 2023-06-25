package com.hotsix.iAmNotAlone.domain.membership.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikesListPostProjectionDto {

    private Long boardId;
    private Long postId;
    private Long regionId;
    private String address;
    private String content;
    private String createdAt;
    private Long memberId;
    private String nickName;
    private Long gender;
    private String userFile;
    private int commentCount;
    private boolean likesFlag;
    private List<String> roomFiles;

}
