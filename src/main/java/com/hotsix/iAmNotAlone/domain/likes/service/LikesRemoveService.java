package com.hotsix.iAmNotAlone.domain.likes.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_POST;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
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
public class LikesRemoveService {

    private final RedisUtil redisUtil;
    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;


    /**
     * 좋아요 카운트--
     */
    @Transactional
    public boolean deleteLike(String postId, String memberId) {

        // id 체크
        if(!postRepository.existsById(Long.valueOf(postId))) {
            throw new BusinessException(NOT_FOUND_POST);
        }

        Membership membership = membershipRepository.findById(Long.valueOf(memberId))
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        String key = getLikeKey(postId);
        redisUtil.removeLike(key);

        // 회원 좋아요 게시글 list
        membership.updateLikeList(postId, false);

        // 변경 사항 반영
        membershipRepository.save(membership);

        log.info("likes decrement");
        return false;
    }


    /**
     * redis key 조합
     */
    private String getLikeKey(String postId) {
        return "likes:" + postId;
    }
}