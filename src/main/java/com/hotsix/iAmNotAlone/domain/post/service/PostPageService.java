package com.hotsix.iAmNotAlone.domain.post.service;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostSettingResponseDto;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostScrollResponseDto;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostPageService {

    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;

    // 페이지
    public List<PostScrollResponseDto> postPagesBy(Long lastPostId, int size, Long userId) {
        Membership loginMember = membershipRepository.findById(userId).get();
        Long memberId = loginMember.getId();
        Page<Post> posts = fetchPages(lastPostId, size, memberId);
        List<Post> content = posts.getContent();

        return content.stream()
                .map(t -> new PostScrollResponseDto(t))
                .collect(Collectors.toList());
    }

    public Page<Post> fetchPages(Long lastPostId, int size, Long loginMemberId) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return postRepository.findByIdLessThanAndMembershipIdOrderByIdDesc(lastPostId, loginMemberId, pageRequest);
    }


    // 페이지 기본 세팅
    public List<PostSettingResponseDto> postBasicSetting(Long userId) {
        Membership loginMember = membershipRepository.findById(userId).get();
        Long loginMemberId = loginMember.getId();
        List<Post> postList = postRepository.findTop5ByMembershipIdOrderByIdDesc(loginMemberId);
        return postList.stream()
                .map(p -> new PostSettingResponseDto(p))
                .collect(Collectors.toList());
    }

}
