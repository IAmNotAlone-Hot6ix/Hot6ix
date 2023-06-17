package com.hotsix.iAmNotAlone.domain.post.service;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.model.dto.PostResponseDto;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostPageService {

    private final PostRepository postRepository;
    private final MembershipRepository membershipRepository;

    // 페이지
    public List<PostResponseDto> postPagesBy(Long lastPostId, int size, Long userId) {
        Membership loginMember = membershipRepository.findById(userId).get();
        List<Membership> membershipList = new ArrayList<>();
        membershipList.add(loginMember);

        Page<Post> posts = fetchPages(lastPostId, size, membershipList);
        List<Post> content = posts.getContent();
        return content.stream()
                .map(t -> new PostResponseDto(t))
                .collect(Collectors.toList());
    }

    public Page<Post> fetchPages(Long lastPostId, int size, List<Membership> loginMember) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return postRepository.findByIdLessThanAndMembershipInOrderByIdDesc(lastPostId, loginMember, pageRequest);
    }

    // 페이지 기본 세팅
    public List<PostResponseDto> postBasicSetting(Long userId) {
        Membership loginMember = membershipRepository.findById(userId).get();
        List<Membership> membershipList = new ArrayList<>();
        membershipList.add(loginMember);

        List<Post> postList = postRepository.findTop5ByMembershipInOrderByIdDesc(membershipList);
        return postList.stream()
                .map(p -> new PostResponseDto(p))
                .collect(Collectors.toList());
    }

}
