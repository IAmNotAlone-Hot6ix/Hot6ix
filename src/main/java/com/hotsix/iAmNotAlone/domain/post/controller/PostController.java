package com.hotsix.iAmNotAlone.domain.post.controller;

import com.hotsix.iAmNotAlone.domain.post.model.dto.PostDetailDto;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostResponseDto;
import com.hotsix.iAmNotAlone.domain.post.model.form.AddPostForm;
import com.hotsix.iAmNotAlone.domain.post.model.form.ModifyPostForm;
import com.hotsix.iAmNotAlone.domain.post.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostRegisterService postRegisterService;
    private final PostDetailService postDetailService;
    private final PostModifyService postModifyService;
    private final PostPageService postPageService;
    private final PostRemoveService postRemoveService;

    @PostMapping(value = "/post/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> postAdd(@PathVariable(name = "userId") Long id,
        @RequestPart AddPostForm form,
        @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) {
        return ResponseEntity.ok(postRegisterService.addPost(id, form, multipartFiles));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDetailDto> postDetails(@PathVariable("postId") Long id) {
        return ResponseEntity.ok(postDetailService.findPost(id));
    }

    @PutMapping(value = "/post/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> postModify(@PathVariable("postId") Long id,
        @RequestPart ModifyPostForm form,
        @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) {
        return ResponseEntity.ok(postModifyService.modifyPost(id, form, multipartFiles));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> postRemove(@PathVariable("postId") Long id) {
        postRemoveService.removePost(id);
        return ResponseEntity.ok("게시글이 삭제 되었습니다.");
    }

    // 게시글 무한스크롤 요청 api
    @GetMapping("/api/post/{userId}")
    public ResponseEntity<Result> getPostLowerThanId(@RequestParam Long lastPostId,
        @RequestParam int size, @PathVariable Long userId) {
        List<PostResponseDto> postResponse = postPageService.postPagesBy(lastPostId, size, userId);
        return ResponseEntity.ok(new Result(postResponse));
    }

    // 마이페이지 게시글 세팅 api
    @GetMapping("/api/post/basic/{userId}")
    public ResponseEntity<Result> getPostInUserId(@PathVariable Long userId) {
        List<PostResponseDto> postResponse = postPageService.postBasicSetting(userId);
        return ResponseEntity.ok(new Result(postResponse));
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}

