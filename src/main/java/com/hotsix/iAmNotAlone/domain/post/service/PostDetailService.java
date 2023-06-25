package com.hotsix.iAmNotAlone.domain.post.service;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostDetailDto;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_POST;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostDetailService {

    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;

    public PostDetailDto findPost(Long postId, Long membershipId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(NOT_FOUND_POST)
        );
        PostDetailDto postDetailDto = PostDetailDto.from(post);
        log.info("게시글 정보 조회");

        Membership membership = membershipRepository.findById(membershipId).orElseThrow(
            () -> new BusinessException(NOT_FOUND_USER)
        );
        List<Long> likeList = membership.getLikelist().stream().map(Long::parseLong).collect(
            Collectors.toList());

        postDetailDto.setLike(likeList.contains(postId));

        return postDetailDto;
    }

}
