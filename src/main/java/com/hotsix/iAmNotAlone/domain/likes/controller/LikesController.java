package com.hotsix.iAmNotAlone.domain.likes.controller;

import com.hotsix.iAmNotAlone.domain.likes.service.LikesRegisterRemoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikesController {

    private final LikesRegisterRemoveService likesRegisterRemoveService;


    @PostMapping("/{post_id}/{user_id}")
    public ResponseEntity<String> addDeleteLikes(@PathVariable Long post_id, @PathVariable Long user_id) {
        return ResponseEntity.ok(
            likesRegisterRemoveService.addDeleteLike(String.valueOf(post_id), String.valueOf(user_id)));
    }
}
