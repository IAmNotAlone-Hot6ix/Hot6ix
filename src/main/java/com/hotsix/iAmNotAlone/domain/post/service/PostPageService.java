package com.hotsix.iAmNotAlone.domain.post.service;

import com.hotsix.iAmNotAlone.domain.comments.repository.CommentsRepository;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipGetInfoForMainService;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostResponseDto;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.exception.business.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostPageService {

    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;
    private final CommentsRepository commentsRepository;
    private final MembershipGetInfoForMainService forMainService;
    private final RegionRepository regionRepository;

    // 페이지
    public List<PostResponseDto> postPagesBy(Long lastPostId, int size, Long userId) {
        Membership membership = membershipRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );
        List<Long> likeList = forMainService.getLikeList(membership.getId());
        Page<Post> posts = fetchPages(lastPostId, size, userId);
        List<Post> postList = posts.getContent();

        List<PostResponseDto> postResponseList = new ArrayList<>();

        for (Post post : postList) {
            Long commentCount = commentsRepository.countByPostId(post.getId());
            boolean likesFlag = !likeList.isEmpty() && likeList.contains(post.getId());
            PostResponseDto postResponseDto = PostResponseDto.of(post, commentCount, likesFlag);
            postResponseList.add(postResponseDto);
        }

        return postResponseList;
    }

    public Page<Post> fetchPages(Long lastPostId, int size, Long loginMemberId) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return postRepository.findByIdLessThanAndMembershipIdOrderByIdDesc(lastPostId, loginMemberId, pageRequest);
    }


    // 페이지 기본 세팅
    public List<PostResponseDto> postBasicSetting(Long userId) {
        Membership membership = membershipRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );

        List<Long> likeList = forMainService.getLikeList(membership.getId());
        List<Post> postList = postRepository.findTop10ByMembershipIdOrderByIdDesc(userId);

        List<PostResponseDto> postResponseList = new ArrayList<>();

        for (Post post : postList) {

            Long commentCount = commentsRepository.countByPostId(post.getId());
            boolean likesFlag = !likeList.isEmpty() && likeList.contains(post.getId());

            PostResponseDto postResponseDto = PostResponseDto.of(post, commentCount, likesFlag);
            postResponseList.add(postResponseDto);
        }
        return postResponseList;
    }

}
