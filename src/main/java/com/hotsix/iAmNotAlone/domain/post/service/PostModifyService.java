package com.hotsix.iAmNotAlone.domain.post.service;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.S3FileDto;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.form.ModifyPostForm;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_POST;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostModifyService {

    private final PostRepository postRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public Long modifyPost(Long id, ModifyPostForm form, List<MultipartFile> multipartFiles) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new BusinessException(NOT_FOUND_POST)
        );
        log.info("게시글 조회");

        if (post.getImgPath().size() >= 1 && post.getImgPath().get(0).length() > 0) {
            HashMap<String, Integer> countImg = new HashMap<>();

            for (String s : post.getImgPath()) {
                countImg.put(s, countImg.getOrDefault(s, 0) + 1);
            }
            for (String s : form.getImgPath()) {
                if (s.length() > 0) {
                    countImg.put(s, countImg.getOrDefault(s, 0) + 1);
                }
            }

            for (String s : countImg.keySet()) {
                if (countImg.get(s) < 2) {
                    String[] split = s.split("com/");
                    s3UploadService.deleteFile(split[1]);
                }
            }

        }

        List<String> imgPath = form.getImgPath();

        // 이미지 전체 업로드
        if (multipartFiles.get(0).getSize() != 0) {
            List<S3FileDto> s3FileDtos = s3UploadService.uploadFiles(multipartFiles);
            for (S3FileDto file : s3FileDtos) {
                imgPath.add(file.getUploadFileUrl());
            }
            log.info("이미지 업로드");
            form.setImgPath(imgPath);
        }

        post.modifyPost(form);
        log.info("게시글 수정");

        return post.getId();
    }

}
