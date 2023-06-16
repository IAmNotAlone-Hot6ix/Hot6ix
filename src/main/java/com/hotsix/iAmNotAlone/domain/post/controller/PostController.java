package com.hotsix.iAmNotAlone.domain.post.controller;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostDetailDto;
import com.hotsix.iAmNotAlone.domain.post.model.form.AddPostForm;
import com.hotsix.iAmNotAlone.domain.post.service.PostDetailService;
import com.hotsix.iAmNotAlone.domain.post.service.PostRegisterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostRegisterService postRegisterService;
    private final PostDetailService postDetailService;

    @PostMapping(value = "/post/{user_id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> postAdd(
        @PathVariable(name = "user_id") Long id,
        @RequestPart AddPostForm form,
        @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) {
        return ResponseEntity.ok(postRegisterService.addPost(id, form, multipartFiles));
    }

    @GetMapping("/post/{post_id}")
    public ResponseEntity<PostDetailDto> postDetails(@PathVariable("post_id") Long id) {
        return ResponseEntity.ok(postDetailService.findPost(id));
    }

}
