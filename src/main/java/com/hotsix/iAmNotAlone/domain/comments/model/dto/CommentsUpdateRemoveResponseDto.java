package com.hotsix.iAmNotAlone.domain.comments.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsUpdateRemoveResponseDto {

    private Long commentId;
    private Long memberId;
}
