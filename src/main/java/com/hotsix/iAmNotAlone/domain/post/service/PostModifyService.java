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

        // 이미지가 존재하면 전제 삭제
        if (form.getImgPath().get(0).length() != 0) {
            for (String url : form.getImgPath()) {
                String[] split = url.split("/");
                String filePath =
                    split[split.length - 4] + "/" + split[split.length - 3] + "/" + split[split.length - 2];
                String fileName = split[split.length - 1];
                log.info(filePath + " " + fileName);
                s3UploadService.deleteFile(filePath, fileName);
            }
            log.info("이미지 전체 삭제");
        }

        // 이미지 전체 업로드
        List<String> files = new ArrayList<>();
        if (multipartFiles.get(0).getSize() != 0) {
            List<S3FileDto> s3FileDtos = s3UploadService.uploadFiles(multipartFiles);
            for (S3FileDto file : s3FileDtos) {
                files.add(file.getUploadFileUrl());
            }
            log.info("이미지 업로드");
            form.setImgPath(files);
        }


        post.modifyPost(form);
        log.info("게시글 수정");

        return post.getId();
    }

}
