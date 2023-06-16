package com.hotsix.iAmNotAlone.domain.post.controller;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostDetailDto;
import com.hotsix.iAmNotAlone.domain.post.model.form.AddPostForm;
import com.hotsix.iAmNotAlone.domain.post.model.form.ModifyPostForm;
import com.hotsix.iAmNotAlone.domain.post.service.PostDetailService;
import com.hotsix.iAmNotAlone.domain.post.service.PostModifyService;
import com.hotsix.iAmNotAlone.domain.post.service.PostRegisterService;
import com.hotsix.iAmNotAlone.domain.post.service.PostRemoveService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostRegisterService postRegisterService;
    private final PostDetailService postDetailService;
    private final PostModifyService postModifyService;
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
}
