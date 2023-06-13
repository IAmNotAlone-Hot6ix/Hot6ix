package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.MemberDto;
import com.hotsix.iAmNotAlone.domain.membership.model.form.UpdateMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping("/members")
    public List<MemberDto> findAll() {
        return membershipService.findAll();
    }

    @PutMapping("/api/member/update/{user_id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long user_id,
        @RequestBody UpdateMembershipForm updateMembershipForm) {

        return ResponseEntity.ok().build();
    }
}
