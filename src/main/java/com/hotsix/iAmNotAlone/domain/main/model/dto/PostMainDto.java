package com.hotsix.iAmNotAlone.domain.main.model.dto;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostMainDto {

    private Long boardId;
    private Long postId;
    private Long regionId;
    private String address;
    private String content;
    private String createdAt;
    private Long memberId;
    private String nickName;
    private int gender;
    private String userFile;
    private Long commentCount;
    private boolean likesFlag;
    private String roomFiles;


    public static PostMainDto of(Post post, Long commentCount, boolean LikesFlag) {
        // 날짜
        LocalDateTime localDateTime = post.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String createAt = localDateTime.format(formatter);

        return PostMainDto.builder()
            .boardId(post.getBoardId())
            .postId(post.getId())
            .regionId(post.getRegionId())
            .address(post.getAddress())
            .content(post.getContent())
            .createdAt(createAt)
            .nickName(post.getMembership().getNickname())
            .gender(post.getMembership().getGender())
            .userFile(post.getMembership().getImgPath())
            .commentCount(commentCount)
            .likesFlag(LikesFlag)
            .roomFiles(post.getImgPath().get(0))
            .build();
    }

}
