package com.hotsix.iAmNotAlone.domain.membership.service;

import com.hotsix.iAmNotAlone.domain.comments.repository.CommentsRepository;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.LikesListDto;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.LikesListPostResponse;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipLikesListService {

    private final MembershipGetInfoForMainService forMainService;

    private final PostRepository postRepository;
    private final CommentsRepository commentsRepository;


    /**
     * 좋아요 한 게시글 조회
     */
    public LikesListPostResponse getPost(Long memberId, Long lastPostId, Pageable pageable) {
        log.info("member likes List enter");

        // 로그인한 회원 좋아요 한 게시글
        List<Long> likesList = forMainService.getLikeList(memberId);
        
        // 조회해온 좋아요 게시글 리스트
        List<Post> postList =
            postRepository.findRecentPostsByLikeList(likesList, lastPostId, pageable);

        List<LikesListDto> likesListDtos = new ArrayList<>();
        for (Post post : postList) {
            Long commentCount = commentsRepository.countByPostId(post.getId());
            boolean likesFlag = !likesList.isEmpty() && likesList.contains(post.getId());

            likesListDtos.add(LikesListDto.of(post, commentCount, likesFlag));
        }

        // 게시글의 마지막 id
        Long newLastPostId =
            postList.size() == 0 ? null
                : postList.get(postList.size() - 1).getId();

        log.info("member likes List end");
        return LikesListPostResponse.of(likesListDtos, newLastPostId);
    }
}
