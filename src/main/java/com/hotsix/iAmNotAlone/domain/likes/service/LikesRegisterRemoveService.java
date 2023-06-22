package com.hotsix.iAmNotAlone.domain.likes.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_POST;
import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.likes.repository.LikesRepository;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesRegisterRemoveService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;


    /**
     * 좋아요 취소/저장
     */
    @Transactional
    public String addDeleteLike(String postId, String userId) {

        // id 체크
        Post post = postRepository.findById(Long.valueOf(postId))
            .orElseThrow(() -> new BusinessException(NOT_FOUND_POST));

        Membership membership = membershipRepository.findById(Long.valueOf(userId))
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        // 좋아요 체크 후 취소/저장
        if (isLiked(postId, userId)) {
            updateLikes(postId, userId, post, membership, false);
            return "좋아요 취소";

        } else {
            updateLikes(postId, userId, post, membership, true);
            return "좋아요 추가";
        }
    }

    /**
     * 좋아요 취소/저장 디테일 로직
     */
    private void updateLikes(String postId, String userId, Post post, Membership membership,
        boolean isLikeOperation) {

        String key = getLikeKey(postId);
        if (isLikeOperation) {
            // redis 추가
            likesRepository.addLike(key, userId);

        } else {
            // redis 삭제
            likesRepository.removeLike(key, userId);
        }

        // 좋아요 수 조회 및 게시글 좋아요 수 수정
        post.updateLikes(likesRepository.getLikeCount(key));

        // 회원 좋아요 게시글 list
        membership.updateLikeList(postId, isLikeOperation);

        // 변경 사항 반영
        postRepository.save(post);
        membershipRepository.save(membership);

    }

    /**
     * 해당 유저 아이디 게시글 좋아요 유무
     */
    public boolean isLiked(String postId, String userId) {
        String key = getLikeKey(postId);
        return likesRepository.isLiked(key, userId);
    }

    /**
     * 게시글의 전체 좋아요 수 조회
     */
    public Long getLikeCount(String postId) {
        String key = getLikeKey(postId);
        return likesRepository.getLikeCount(key);
    }

    /**
     * 게시글의 좋아요 한 유저 아이디 조회
     */
    public Set<String> getSetValues(String postId) {
        String key = getLikeKey(postId);
        return likesRepository.getSetValues(key);
    }

    /**
     * redis key 조합
     */
    private String getLikeKey(String postId) {
        return "like:post:" + postId;
    }
}
