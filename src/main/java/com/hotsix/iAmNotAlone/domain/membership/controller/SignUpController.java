package com.hotsix.iAmNotAlone.domain.membership.controller;

import com.hotsix.iAmNotAlone.domain.membership.model.form.AddMembershipForm;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.MemberDto;
import com.hotsix.iAmNotAlone.domain.membership.service.SignUpService;

import java.io.IOException;
import java.util.List;

import com.hotsix.iAmNotAlone.global.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;
    private final JwtService jwtService;

    @PostMapping(value = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MemberDto> signUp(@RequestPart AddMembershipForm form,
        @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles) {
        return ResponseEntity.ok(MemberDto.from(signUpService.signUp(form, multipartFiles)));
    }

    @PostMapping("/refresh")
    public void refresh(HttpServletRequest request , HttpServletResponse response) throws IOException {
        String refresh = jwtService.refresh(request, response);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(refresh);
    }
}
