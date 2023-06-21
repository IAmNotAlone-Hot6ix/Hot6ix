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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsRegisterService {

    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;
    private final CommentsRepository commentsRepository;

    @Transactional
    public Long addComments(Long postId, Long userId, CommentRequestForm form) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_POST));

        Membership membership = membershipRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );

        Comments comments = commentsRepository.save(Comments.createComments(form, membership, post));
        return comments.getId();
    }

    @Transactional
    public List<CommentsDetailResponseDto> getCommentsDetail(Long postId) {

        List<Comments> comments = commentsRepository.findByPost(postId);

        return comments.stream().map(
                        c -> new CommentsDetailResponseDto(c))
                .collect(Collectors.toList());
    }
}
