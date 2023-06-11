package com.hotsix.iAmNotAlone.domain.main.model.dto;

public interface PostProjection {

    Long getBoard_id();
    Long getPost_id();
    Long getRegion_id();
    String getAddress();
    String getContent();
    String getCreated_at();
    Long getUser_id();
    String getNick_name();
    int getGender();
    String getUser_file();
    int getComment_count();
    String getStr_likes();
    String getRoom_files();

}
