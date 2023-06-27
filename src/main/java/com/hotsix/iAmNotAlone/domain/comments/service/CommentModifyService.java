package com.hotsix.iAmNotAlone.domain.comments.service;

import com.hotsix.iAmNotAlone.domain.comments.entity.Comments;
import com.hotsix.iAmNotAlone.domain.comments.model.dto.CommentsUpdateRemoveResponseDto;
import com.hotsix.iAmNotAlone.domain.comments.model.form.CommentUpdateRequestForm;
import com.hotsix.iAmNotAlone.domain.comments.repository.CommentsRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentModifyService {

    private final CommentsRepository commentsRepository;

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentsUpdateRemoveResponseDto modifyComment(Long commentId, CommentUpdateRequestForm form) {

        Comments comments = commentsRepository.findById(commentId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_COMMENT)
        );

        comments.updateContent(form.getContent());

        return CommentsUpdateRemoveResponseDto.builder()
                .memberId(comments.getMembership().getId())
                .commentId(comments.getId())
                .build();
    }
}
