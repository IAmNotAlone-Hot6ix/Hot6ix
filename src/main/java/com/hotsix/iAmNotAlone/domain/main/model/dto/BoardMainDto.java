package com.hotsix.iAmNotAlone.domain.main.model.dto;

import com.hotsix.iAmNotAlone.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardMainDto {

    Long board_id;
    String name;

    public static BoardMainDto from(Board board) {
        return BoardMainDto.builder()
            .board_id(board.getId())
            .name(board.getName())
            .build();
    }

}
