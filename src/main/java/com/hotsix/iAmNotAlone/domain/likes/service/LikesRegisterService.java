package com.hotsix.iAmNotAlone.domain.likes.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_POST;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.likes.entity.Likes;
import com.hotsix.iAmNotAlone.domain.likes.repository.LikesRepository;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
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
        if(!postRepository.existsById(postId)) {
            throw new BusinessException(NOT_FOUND_POST);
        }

        if(!membershipRepository.existsById(memberId)) {
            throw new BusinessException(NOT_FOUND_USER);
        }

        // redis 게시글의 좋아요 수 count++
        redisUtil.addLike(String.valueOf(postId));

        // 회원 좋아요한 게시글 저장
        likesRepository.save(Likes.builder()
            .memberId(memberId)
            .postId(postId)
            .build());

        log.info("Successfully added like for post: {} by member: {}", postId, memberId);
        return true;
    }
}