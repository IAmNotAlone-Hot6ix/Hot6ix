package com.hotsix.iAmNotAlone.domain.post.model.dto;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDto {

    private String nickname;
    private String img_path;
    private int gender;
    private LocalDateTime createdAt;
    private String content;
    private int commentCount;

    public PostResponseDto(Post post) {

        nickname = post.getMembership().getNickname();
        img_path = post.getMembership().getImgPath();
        gender = post.getMembership().getGender();
        createdAt = post.getCreatedAt();
        content = removeContent(post.getContent());
        commentCount = post.getCommentsList().size();
    }

    public String removeContent(String content) {
        if (content.length() > 30) {
            content = content.substring(0, 30);
            content += "...더보기";
        }
        return content;
    }
}
