package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.MemberDto;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MyMemberDto;
import com.hotsix.iAmNotAlone.domain.membership.model.form.UpdateMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.UpdatePasswordForm;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping("/memberships")
    public List<MemberDto> findAll() {
        return membershipService.findAll();
    }

    @PutMapping(value = "/api/membership/update/{user_id}", consumes = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long user_id,
                                                  @RequestPart UpdateMembershipForm form,
                                                  @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) {
        return ResponseEntity.ok(membershipService.update(user_id, form, multipartFiles));
    }

    @PutMapping("/api/membership/update/password/{user_id}")
    public ResponseEntity<String> updatePassword(@PathVariable Long user_id, @RequestBody
    UpdatePasswordForm form) {
        membershipService.updatePassword(user_id, form);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/membership/delete/{user_id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long user_id) {
        membershipService.delete(user_id);
        return ResponseEntity.ok().build();
    }

    // 마이페이지 유저 정보 api
    @GetMapping("/api/my/{user_id}")
    public ResponseEntity<MyMemberDto> myMember(@PathVariable Long user_id) {
        return ResponseEntity.ok(membershipService.my(user_id));
    }
}
