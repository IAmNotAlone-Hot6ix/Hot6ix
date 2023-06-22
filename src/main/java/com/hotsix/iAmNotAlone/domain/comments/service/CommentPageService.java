package com.hotsix.iAmNotAlone.domain.comments.service;

import com.hotsix.iAmNotAlone.domain.comments.entity.Comments;
import com.hotsix.iAmNotAlone.domain.comments.model.dto.CommentsDetailResponseDto;
import com.hotsix.iAmNotAlone.domain.comments.repository.CommentsRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentPageService {

    private final PostRepository postRepository;
    private final CommentsRepository commentsRepository;

    // 페이지
    public List<CommentsDetailResponseDto> commentPagesBy(Long lastCommentId, int size, Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_POST)
        );
        Long postNum = post.getId();
        Page<Comments> comments = fetchPages(lastCommentId, size, postNum);
        List<Comments> content = comments.getContent();

        return content.stream()
                .map(c -> new CommentsDetailResponseDto(c))
                .collect(Collectors.toList());
    }

    public Page<Comments> fetchPages(Long lastCommentId, int size, Long postId) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return commentsRepository.findByIdGreaterThanAndPostIdOrderByIdAsc(lastCommentId, postId, pageRequest);
    }

    // 댓글 기본 세팅
    public List<CommentsDetailResponseDto> commentBasicSetting(Long postId) {

        List<Comments> commentsList = commentsRepository.findTop10ByPostIdOrderByIdAsc(postId);
        return commentsList.stream()
                .map(c -> new CommentsDetailResponseDto(c))
                .collect(Collectors.toList());
    }


}
