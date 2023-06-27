package com.hotsix.iAmNotAlone.domain.comments.service;

import com.hotsix.iAmNotAlone.domain.comments.entity.Comments;
import com.hotsix.iAmNotAlone.domain.comments.repository.CommentsRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentRemoveService {

    private final CommentsRepository commentsRepository;

    @Transactional
    public Long removeComment(Long commentId) {
        Comments comments = commentsRepository.findById(commentId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_COMMENT)
        );
        commentsRepository.delete(comments);

        return comments.getId();
    }
}
