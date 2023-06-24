package com.hotsix.iAmNotAlone.domain.likes.controller;

import com.hotsix.iAmNotAlone.domain.likes.service.LikesRegisterService;
import com.hotsix.iAmNotAlone.domain.likes.service.LikesRemoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikesController {

    private final LikesRegisterService likesRegisterService;
    private final LikesRemoveService likesRemoveService;


    /**
     * 좋아요
     */
    @PostMapping("/{post_id}/{user_id}")
    public ResponseEntity<Boolean> addLikes(@PathVariable Long post_id,
        @PathVariable Long user_id) {
        return ResponseEntity.ok(
            likesRegisterService.addLike(String.valueOf(post_id),
                String.valueOf(user_id)));
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping("/{post_id}/{user_id}")
    public ResponseEntity<Boolean> deleteLikes(@PathVariable Long post_id,
        @PathVariable Long user_id) {
        return ResponseEntity.ok(
            likesRemoveService.deleteLike(String.valueOf(post_id),
                String.valueOf(user_id)));
    }
}