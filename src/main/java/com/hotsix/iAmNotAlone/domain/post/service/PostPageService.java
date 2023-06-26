package com.hotsix.iAmNotAlone.domain.post.service;

import com.hotsix.iAmNotAlone.domain.comments.repository.CommentsRepository;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostResponseDto;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostPageService {

    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;
    private final CommentsRepository commentsRepository;

    // 페이지
    public List<PostResponseDto> postPagesBy(Long lastPostId, int size, Long userId) {
        membershipRepository.findById(userId).orElseThrow(
                ()->new BusinessException(ErrorCode.NOT_FOUND_USER)
        );
        Page<Post> posts = fetchPages(lastPostId, size, userId);
        List<Post> postList = posts.getContent();

        List<PostResponseDto> postResponseList = new ArrayList<>();

        for (Post post : postList) {
            PostResponseDto postResponseDto = new PostResponseDto(post);
            Long commentCount = commentsRepository.countByPostId(post.getId());
            postResponseDto.setCommentCount(commentCount);
            postResponseList.add(postResponseDto);
        }
        return postResponseList;
    }

    public Page<Post> fetchPages(Long lastPostId, int size, Long loginMemberId) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return postRepository.findByIdLessThanAndMembershipIdOrderByIdDesc(lastPostId, loginMemberId, pageRequest);
    }


    // 페이지 기본 세팅
    public List<PostResponseDto> postBasicSetting(Long userId) {
        membershipRepository.findById(userId).orElseThrow(
                ()->new BusinessException(ErrorCode.NOT_FOUND_USER)
        );
        List<Post> postList = postRepository.findTop10ByMembershipIdOrderByIdDesc(userId);

        List<PostResponseDto> postResponseList = new ArrayList<>();

        for (Post post : postList) {
            PostResponseDto postResponseDto = new PostResponseDto(post);
            Long commentCount = commentsRepository.countByPostId(post.getId());
            postResponseDto.setCommentCount(commentCount);
            postResponseList.add(postResponseDto);
        }
        return postResponseList;
    }

}
