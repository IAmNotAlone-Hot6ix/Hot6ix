package com.hotsix.iAmNotAlone.domain.likes.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_POST;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.likes.entity.Likes;
import com.hotsix.iAmNotAlone.domain.likes.repository.LikesRepository;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.RedisUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LikesRemoveAllService {

    private final RedisUtil redisUtil;
    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;
    private final LikesRepository likesRepository;


    /**
     * 좋아요 삭제 (게시글 삭제시 회원이 작성한 게시글 기준)
     */
    @Transactional
    public void deletePostLikes(Long postId) {

        if(!postRepository.existsById(postId)) {
            throw new BusinessException(NOT_FOUND_POST);
        }

        log.info("likes deleteAllByPostId");

        // 좋아요 삭제 (게시글 ID 기준)
        likesRepository.deleteAllByPostId(postId);
    }

    /**
     * 좋아요 count-- (회원 탈퇴 시 회원이 좋아요한 게시글 기준)
     */
    @Transactional
    public void deleteLikesByMemberId(Long memberId) {

        if(!membershipRepository.existsById(memberId)) {
            throw new BusinessException(NOT_FOUND_USER);
        }

        List<Likes> likesList = likesRepository.findByMemberId(memberId);

        log.info("likes decrement by postCount");
        for (Likes like : likesList) {
            // redis 게시글의 좋아요 수 count--
            String key = redisUtil.getLikeKey(String.valueOf(like.getPostId()));
            redisUtil.removeLike(key);
        }

        likesRepository.deleteAll(likesList);
    }
}