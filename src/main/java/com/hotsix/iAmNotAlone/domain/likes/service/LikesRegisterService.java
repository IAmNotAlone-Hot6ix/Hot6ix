package com.hotsix.iAmNotAlone.domain.likes.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_POST;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.likes.entity.Likes;
import com.hotsix.iAmNotAlone.domain.likes.repository.LikesRepository;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LikesRegisterService {

    private final RedisUtil redisUtil;
    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;
    private final LikesRepository likesRepository;


    /**
     * 좋아요 카운트++
     */
    @Transactional
    public boolean addLike(Long postId, Long memberId) {

        // id 체크
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_POST));

        Membership membership = membershipRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        // redis 게시글의 좋아요 수 count++
        String key = redisUtil.getLikeKey(String.valueOf(postId));
        redisUtil.addLike(key);

        // 회원 좋아요한 게시글 저장
        likesRepository.save(Likes.builder()
            .memberId(membership.getId())
            .postId(post.getId())
            .build());

        log.info("likes increment");
        return true;
    }

}