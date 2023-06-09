package com.hotsix.iAmNotAlone.domain.main.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.comments.repository.CommentsRepository;
import com.hotsix.iAmNotAlone.domain.likes.service.LikesGetPostId;
import com.hotsix.iAmNotAlone.domain.main.model.dto.MainPostResponse;
import com.hotsix.iAmNotAlone.domain.main.model.dto.MainResponse;
import com.hotsix.iAmNotAlone.domain.main.model.dto.PostMainDto;
import com.hotsix.iAmNotAlone.domain.main.model.dto.RegionMainDto;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.membership.service.MembershipGetInfoForMainService;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final MembershipGetInfoForMainService forMainService;
    private final LikesGetPostId likesGetPostId;

    private final RegionRepository regionRepository;
    private final MembershipRepository membershipRepository;
    private final PostRepository postRepository;
    private final CommentsRepository commentsRepository;


    /**
     * 메인화면 컨트롤 데이터 바인딩용 조회
     *
     * @param memberId 회원 아이디
     * @return MainResponse
     */
    public MainResponse getMain(Long memberId) {

        // membership
        Membership membership = membershipRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        // 지역 리스트
        List<Region> regionList = regionRepository.findAll();

        return MainResponse.of(
            regionList.stream().map(RegionMainDto::from).collect(Collectors.toList()),
            membership.getRegion().getId());
    }

    /**
     * 게시글 조회
     * @param memberId   회원 아이디
     * @param regionId 지역 아이디
     * @param boardId  게시판 아이디
     * @param pageable 페이징 페이지, 크기
     * @return MainPostResponse
     */
    public MainPostResponse getPost(Long memberId, Long regionId, Long boardId, Long lastPostId,
        Pageable pageable) {
        log.info("Main enter");

        // 로그인한 회원의 게시글
        List<Long> likesList = likesGetPostId.getLikeList(memberId);

        // 로그인한 회원의 성별
        int gender = forMainService.getGender(memberId);
        log.debug("membership.getGender(): " + gender);

        // 게시글 리스트
        List<Post> postList =
            postRepository.findRecentPostsByRegionAndBoard(regionId, boardId, lastPostId, pageable);

        List<PostMainDto> postMainDtoList = new ArrayList<>();
        for(Post post : postList) {
            if(post.getMembership().getGender() == gender) {
                Long commentCount = commentsRepository.countByPostId(post.getId());
                boolean likesFlag = !likesList.isEmpty() && likesList.contains(post.getId());

                postMainDtoList.add(PostMainDto.of(post, commentCount, likesFlag));
            }
        }

        // 게시글의 마지막 id
        Long newLastPostId =
            postList.size() == 0 ? null
                : postList.get(postList.size() - 1).getId();

        return MainPostResponse.of(postMainDtoList, newLastPostId);
    }
}
