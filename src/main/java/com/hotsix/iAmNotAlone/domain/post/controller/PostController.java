package com.hotsix.iAmNotAlone.domain.post.controller;

import com.hotsix.iAmNotAlone.domain.post.model.dto.PostDetailDto;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostResponseDto;
import com.hotsix.iAmNotAlone.domain.post.model.form.AddPostForm;
import com.hotsix.iAmNotAlone.domain.post.model.form.ModifyPostForm;
import com.hotsix.iAmNotAlone.domain.post.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/")
public class PostController {

    private final PostRegisterService postRegisterService;
    private final PostDetailService postDetailService;
    private final PostModifyService postModifyService;
    private final PostPageService postPageService;
    private final PostRemoveService postRemoveService;

    @PostMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> postAdd(@PathVariable(name = "userId") Long id,
        @RequestPart AddPostForm form,
        @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) {

        return ResponseEntity.ok(postRegisterService.addPost(id, form, multipartFiles));
    }

    @GetMapping("/{postId}/{membershipId}")
    public ResponseEntity<PostDetailDto> postDetails(@PathVariable Long postId,
        @PathVariable Long membershipId) {

        return ResponseEntity.ok(postDetailService.findPost(postId, membershipId));
    }

    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> postModify(@PathVariable("postId") Long id,
        @RequestPart ModifyPostForm form,
        @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) {

        return ResponseEntity.ok(postModifyService.modifyPost(id, form, multipartFiles));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> postRemove(@PathVariable("postId") Long id) {
        postRemoveService.removePost(id);
        return ResponseEntity.ok("게시글이 삭제 되었습니다.");
    }

    // 게시글 무한스크롤 요청 api
    @GetMapping("/{userId}")
    public ResponseEntity<Result<List<PostResponseDto>>> getPostLowerThanId(@RequestParam Long lastPostId,
                                                                            @RequestParam int size, @PathVariable Long userId) {
        List<PostResponseDto> postResponse = postPageService.postPagesBy(lastPostId, size, userId);
        return ResponseEntity.ok(new Result(postResponse));
    }

    // 마이페이지 게시글 세팅 api
    @GetMapping("/basic/{userId}")
    public ResponseEntity<Result<List<PostResponseDto>>> getPostInUserId(@PathVariable Long userId) {
        List<PostResponseDto> postResponse = postPageService.postBasicSetting(userId);
        return ResponseEntity.ok(new Result(postResponse));
    }

    @Data
    @AllArgsConstructor
    public class Result<T> {
        private T data;
    }

}

