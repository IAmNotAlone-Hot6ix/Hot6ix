package com.hotsix.iAmNotAlone.domain.main.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostProjectionMainDto {

    Long board_id;
    Long post_id;
    Long region_id;
    String address;
    String content;
    String created_at;
    Long user_id;
    String nick_name;
    int gender;
    String user_file;
    int comment_count;
    boolean likes;
    List<String> room_files;

}
