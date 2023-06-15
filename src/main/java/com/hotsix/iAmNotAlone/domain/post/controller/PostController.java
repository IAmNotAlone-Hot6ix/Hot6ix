package com.hotsix.iAmNotAlone.domain.post.controller;

import com.hotsix.iAmNotAlone.domain.post.model.dto.PostDetailDto;
import com.hotsix.iAmNotAlone.domain.post.model.form.AddPostForm;
import com.hotsix.iAmNotAlone.domain.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/post/{user_id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostDetailDto> addPost(
        @PathVariable(name = "user_id") Long id,
        @RequestPart AddPostForm form,
        @RequestPart(value = "files", required = false) List<MultipartFile> multipartFiles
    ) {
        return ResponseEntity.ok(
            PostDetailDto.from(postService.addPost(id, form, multipartFiles)));
    }
}
