package com.hotsix.iAmNotAlone.domain.post.service;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostRemoveService {

    private final PostRepository postRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public void removePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
            () -> new BusinessException(ErrorCode.NOT_FOUND_POST)
        );

        if (post.getImgPath().get(0).length() != 0) {
            for (String url : post.getImgPath()) {
                String[] split = url.split("com/");
                s3UploadService.deleteFile(split[1]);
            }
        }
        postRepository.deleteById(id);
    }
}
