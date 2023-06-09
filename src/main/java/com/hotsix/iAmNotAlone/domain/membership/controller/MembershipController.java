package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.LikesListPostResponse;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MembershipDetailDto;
import com.hotsix.iAmNotAlone.domain.membership.model.form.ModifyMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.ModifyPasswordForm;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipLikesListService;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipModifyService;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipRemoveService;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipDetailService;
import com.hotsix.iAmNotAlone.domain.membership.service.PasswordModifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/membership")
public class MembershipController {

    private final MembershipDetailService membershipDetailService;
    private final MembershipModifyService membershipModifyService;
    private final MembershipRemoveService membershipRemoveService;
    private final PasswordModifyService passwordModifyService;
    private final MembershipLikesListService membershipLikesListService;


    /**
     * 마이페이지 유저 정보 api, 회원 조회
     */
    @GetMapping("/detail/{userId}")
    public ResponseEntity<MembershipDetailDto> membershipDetails(@PathVariable Long userId) {
        return ResponseEntity.ok(membershipDetailService.findMembership(userId));
    }

    /**
     * 회원 수정
     */
    @PutMapping(value = "/update/{userId}", consumes = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> membershipModify(@PathVariable Long userId,
        @RequestPart ModifyMembershipForm form,
        @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) {
        return ResponseEntity.ok(
            membershipModifyService.modifyMembership(userId, form, multipartFiles));
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> membershipRemove(@PathVariable Long userId) {
        membershipRemoveService.removeMembership(userId);
        return ResponseEntity.ok("회원탈퇴가 정상적으로 완료되었습니다.");
    }

    /**
     * 비밀번호 수정
     */
    @PutMapping("/update/password/{userId}")
    public ResponseEntity<String> passwordModify(@PathVariable Long userId,
        @RequestBody ModifyPasswordForm form) {
        passwordModifyService.modifyPassword(userId, form);
        return ResponseEntity.ok().build();
    }


    /**
     * 관심목록 페이지 조회
     */
    @GetMapping("/like/{memberId}")
    public ResponseEntity<LikesListPostResponse> getLikesList(@PathVariable Long memberId,
        @RequestParam(required = false) Long lastPostId, @RequestParam int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(
            membershipLikesListService.getPost(memberId, lastPostId, pageable));
    }

}
