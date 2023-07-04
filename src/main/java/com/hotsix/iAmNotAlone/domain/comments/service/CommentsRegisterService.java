package com.hotsix.iAmNotAlone.domain.comments.service;

import com.hotsix.iAmNotAlone.domain.comments.entity.Comments;
import com.hotsix.iAmNotAlone.domain.comments.model.dto.CommentsDetailResponseDto;
import com.hotsix.iAmNotAlone.domain.comments.model.form.CommentRequestForm;
import com.hotsix.iAmNotAlone.domain.comments.repository.CommentsRepository;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsRegisterService {

    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;
    private final CommentsRepository commentsRepository;

    @Transactional
    public CommentsDetailResponseDto addComments(Long postId, Long userId, CommentRequestForm form) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_POST));

        Membership membership = membershipRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );

        Comments comments = commentsRepository.save(Comments.createComments(form, membership, post));

        return CommentsDetailResponseDto.builder()
                .nickName(comments.getMembership().getNickname())
                .content(comments.getContent())
                .imgPath(comments.getMembership().getImgPath())
                .createdAt(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .format(comments.getCreatedAt()))
                .commentId(comments.getId())
                .memberId(comments.getMembership().getId())
                .build();


    }

}
