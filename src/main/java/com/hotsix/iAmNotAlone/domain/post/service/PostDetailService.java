package com.hotsix.iAmNotAlone.domain.post.service;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostDetailDto;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_POST;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostDetailService {

    private final PostRepository postRepository;

    public PostDetailDto findPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new BusinessException(NOT_FOUND_POST)
        );
        log.info("게시글 정보 조회");

        return PostDetailDto.from(post);
    }

}
