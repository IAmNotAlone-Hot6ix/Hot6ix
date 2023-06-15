package com.hotsix.iAmNotAlone.domain.post.model.form;

import lombok.Getter;

@Getter
public class AddPostForm {

    private Long boardId;
    private Long regionId;
    private String address;
    private String content;
    private int gender;
}
