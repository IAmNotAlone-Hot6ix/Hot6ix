package com.hotsix.iAmNotAlone.domain.personality.controller;

import com.hotsix.iAmNotAlone.domain.personality.model.dto.UserRecommendationDto;
import com.hotsix.iAmNotAlone.domain.personality.service.PreferPersonalityGetMembership;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/personality")
public class PersonalityController {

    private final PreferPersonalityGetMembership preferPersonalityGetMembership;


    /**
     * 유저의 선호 성향에 맞는 유저 조회 (랜덤)
     */
    @GetMapping("/{memberId}/{size}")
    public ResponseEntity<List<UserRecommendationDto>> getPreferPersonality(
        @PathVariable Long memberId, @PathVariable Long size) {
        return ResponseEntity.ok(
            preferPersonalityGetMembership.getPreferMembership(memberId, size));
    }

}
