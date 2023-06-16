package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.MemberDto;
import com.hotsix.iAmNotAlone.domain.membership.model.form.UpdateMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.form.UpdatePasswordForm;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        return ResponseEntity.ok("회원탈퇴가 정상적으로 완료되었습니다.");
    }
}
