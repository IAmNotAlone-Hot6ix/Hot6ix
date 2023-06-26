package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipGetInfoForMainService {

    private final MembershipRepository membershipRepository;


    /**
     * 회원이 좋아요 한 게시글 리스트 조회
     */
    public List<Long> getLikeList(Long memberId) {

        // membership
        Membership membership = membershipRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        // 회원이 좋아요 한 게시글 리스트
        return membership.getLikelist().stream()
            .filter(s -> !s.isEmpty())   // 빈 문자열 제외
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }

    /**
     * 회원의 성별 조회
     */
    public int getGender(Long memberId) {
        // membership
        Membership membership = membershipRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        return membership.getGender();
    }

}
