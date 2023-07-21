package com.hotsix.iAmNotAlone.domain.personality.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.personality.model.dto.UserPersonalityDto;
import com.hotsix.iAmNotAlone.domain.personality.model.dto.UserRecommendationDto;
import com.hotsix.iAmNotAlone.domain.personality.type.MbtiGoodType;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class PreferPersonalityGetMembership {

    private final MembershipRepository membershipRepository;

    public List<UserRecommendationDto> getPreferMembership(Long memberId, Long size) {

        log.info("getPreferMembership enter");
        Membership membership = membershipRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        // 로그인한 유저의 지역, 성별 기준 회원 전체 조회
        List<Membership> membershipList =
            membershipRepository.findAllByIdNotAndRegionIdAndGender(membership.getId(),
                membership.getRegion().getId(), membership.getGender());

        // 리스트 필터 상위 size 명의 회원만 선택
        return getRecommendedMembers(membership, membershipList, size);
    }

    private List<UserRecommendationDto> getRecommendedMembers(Membership membership,
        List<Membership> memberships, Long size) {

        log.info("getRecommendedMembers enter");
        // 유저별 추천 점수 계산
        List<MatchCountMembership> matchCountMemberships = new ArrayList<>();
        for (Membership m : memberships) {
            matchCountMemberships.add(
                new MatchCountMembership(m, calculateMatchCount(m, membership)));
        }

        // 제일 높은 점수 기준 정렬
        matchCountMemberships.sort(
            Comparator.comparingInt(MatchCountMembership::getMatchCount).reversed());
        // 반환 List
        List<UserRecommendationDto> recommendationDtos = new ArrayList<>();

        // 최대 점수
        int highestScore = matchCountMemberships.get(0).getMatchCount();

        List<MatchCountMembership> highestScoreMemberships = new ArrayList<>();
        for (MatchCountMembership matchCountMembership : matchCountMemberships) {
            if (matchCountMembership.getMatchCount() == highestScore) {
                highestScoreMemberships.add(matchCountMembership);
            } else {
                break;
            }
        }

        // 최대점수 인원이 size 보다 클 경우
        if (highestScoreMemberships.size() > size) {

            // 가장 높은 점수를 가진 사람들 중에서 랜덤하게 선택
            Collections.shuffle(highestScoreMemberships);
            highestScoreMemberships = highestScoreMemberships.subList(0, size.intValue());

            for (MatchCountMembership matchCountMembership : highestScoreMemberships) {
                // 반환 DTO 로 변경
                recommendationDtos.add(convertToDto(matchCountMembership.getMembership()));
            }

        } else {
            for (MatchCountMembership matchCountMembership
                : matchCountMemberships.subList(0, size.intValue())) {
                // 반환 DTO 로 변경
                recommendationDtos.add(convertToDto(matchCountMembership.getMembership()));
            }
        }

        System.out.println("recommendationDtos: " + recommendationDtos);
        return recommendationDtos;
    }

    /**
     * 추천 점수 계산
     */
    private int calculateMatchCount(Membership PreperMembership, Membership loginMembership) {
        log.info("calculateMatchCount enter");
        int count = 0;

        int age = Period.between(PreperMembership.getBirth(), LocalDate.now()).getYears();
        int preferAge = loginMembership.getPersonality().getPreferAge();

        String memberMbti = loginMembership.getPersonality().getMbti();
        List<String> goodMbti = MbtiGoodType.valueOf(memberMbti).getMatches();

        // MBTI
        if (goodMbti.contains(PreperMembership.getPersonality().getMbti())) {
            count++;
        }

        // 흡연여부 (선호 흡연여부가 같거나 3(상관없음) 일 경우)
        if (loginMembership.getPersonality().getPreferSmoking()
            == PreperMembership.getPersonality().getPreferSmoking()
            || loginMembership.getPersonality().getPreferSmoking() == 3) {
            count++;
        }

        // 활동시간
        if (loginMembership.getPersonality().getPreferActiveTime()
            == PreperMembership.getPersonality().getActiveTime()) {
            count++;
        }

        // 반려동물유무 (선호 반려동물유무가 같거나 3(상관없음) 일 경우)
        if (loginMembership.getPersonality().getPreferPets()
            == PreperMembership.getPersonality().getPets()
            || loginMembership.getPersonality().getPreferPets() == 3) {
            count++;
        }

        // 나이
        if (age >= preferAge && age < preferAge + 10) {
            count++;
        }

        return count;
    }

    /**
     * 반환 DTO 로 변환
     */
    private UserRecommendationDto convertToDto(Membership membership) {
        log.info("UserRecommendationDto enter");

        return UserRecommendationDto.builder()
            .member_id(membership.getId())
            .nickName(membership.getNickname())
            .user_image(membership.getImgPath())
            .age(Period.between(membership.getBirth(), LocalDate.now()).getYears())
            .region(membership.getRegion().getSido() + " " + membership.getRegion().getSigg())
            .personality(UserPersonalityDto.builder()
                .userPersonalityId(membership.getPersonality().getId())
                .mbti(membership.getPersonality().getMbti())
                .smoking(membership.getPersonality().getSmoking())
                .activeTime(membership.getPersonality().getActiveTime())
                .pets(membership.getPersonality().getPets())
                .build())
            .build();
    }

    /**
     * 점수 클래스
     */
    @Getter
    @AllArgsConstructor
    private static class MatchCountMembership {

        private final Membership membership;
        private final int matchCount;
    }
}
