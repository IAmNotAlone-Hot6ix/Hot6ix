package com.hotsix.iAmNotAlone.domain.post.model.dto;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDto {

    private String nickname;
    private String imgPath;
    private int gender;
    private LocalDateTime createdAt;
    private String content;
    private Long commentCount;
    private Long postId;
    private String postImgPath;
    private boolean likeYn;

    public PostResponseDto(Post post) {

        nickname = post.getMembership().getNickname();
        imgPath = post.getMembership().getImgPath();
        gender = post.getMembership().getGender();
        createdAt = post.getCreatedAt();
        content = removeContent(post.getContent());
        postId = post.getId();
        postImgPath = post.getImgPath().get(0);
        likeYn = likeCheck(post.getLikes());
    }

    public String removeContent(String content) {
        if (content.length() > 30) {
            content = content.substring(0, 30);
            content += "...더보기";
        }
        return content;
    }

    public boolean likeCheck(Long likeLength){
        if (likeLength>0){
            return true;
        }
        return false;
    }

}
