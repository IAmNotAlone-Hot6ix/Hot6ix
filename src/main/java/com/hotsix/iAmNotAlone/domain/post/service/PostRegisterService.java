package com.hotsix.iAmNotAlone.domain.post.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.S3FileDto;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.form.AddPostForm;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditOverride;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostRegisterService{

    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public Long addPost(Long id, AddPostForm form, List<MultipartFile> multipartFiles) {

        Membership membership = membershipRepository.findById(id).orElseThrow(
            () -> new BusinessException(NOT_FOUND_USER)
        );
        log.info("회원정보 조회");

        List<String> files = new ArrayList<>();
        if (multipartFiles.get(0).getSize() != 0) {
            List<S3FileDto> s3FileDtos = s3UploadService.uploadFiles(multipartFiles);
            for (S3FileDto file : s3FileDtos) {
                files.add(file.getUploadFileUrl());
            }
        }
        log.info("파일업로드 성공");

        Post post = postRepository.save(Post.createPost(form, membership, files));
        log.info("유저정보 저장");

        return post.getId();
    }

}
