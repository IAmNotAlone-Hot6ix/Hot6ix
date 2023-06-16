package com.hotsix.iAmNotAlone.domain.post.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.S3FileDto;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.form.ModifyPostForm;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
            () -> new BusinessException(NOT_FOUND_USER)
        );
        log.info("게시글 조회");

        //이미지 파일 새 리스트에
//        List<String> images = new ArrayList<>();
//        for (String image : form.getImgPath()) {
//            images.add(image);
//        }
//
//        if (multipartFiles.get(0).getSize() != 0) {
//            List<S3FileDto> s3FileDtos = s3UploadService.uploadFiles(multipartFiles);
//            for (S3FileDto file : s3FileDtos) {
//
//            }
//        }



        post.modifyPost(form);
        log.info("게시글 수정");

        return post.getId();
    }

}
