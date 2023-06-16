package com.hotsix.iAmNotAlone.domain.post.model.form;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyPostForm {

    private Long boardId;
    private Long regionId;
    private String address;
    private String content;
    private List<String> imgPath;
}
