package com.hotsix.iAmNotAlone.domain.likes.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.likes.entity.Likes;
import com.hotsix.iAmNotAlone.domain.likes.repository.LikesRepository;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesGetPostId {

    private final LikesRepository likesRepository;
    private final MembershipRepository membershipRepository;


    /**
     * 회원이 좋아요 한 게시글 리스트 조회
     */
    public List<Long> getLikeList(Long memberId) {

        // membership
        if(!membershipRepository.existsById(memberId)) {
            throw new BusinessException(NOT_FOUND_USER);
        }

        // 회원이 좋아요 한 게시글 리스트
        return likesRepository.findByMemberId(memberId)
            .stream()
            .map(Likes::getPostId)
            .collect(Collectors.toList());
    }

}
