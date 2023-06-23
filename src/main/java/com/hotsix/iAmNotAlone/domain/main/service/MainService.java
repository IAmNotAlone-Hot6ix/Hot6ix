package com.hotsix.iAmNotAlone.domain.main.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.main.model.dto.MainPostResponse;
import com.hotsix.iAmNotAlone.domain.main.model.dto.MainResponse;
import com.hotsix.iAmNotAlone.domain.main.model.dto.PostProjection;
import com.hotsix.iAmNotAlone.domain.main.model.dto.PostProjectionMainDto;
import com.hotsix.iAmNotAlone.domain.main.model.dto.RegionMainDto;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.ListToStringConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final RegionRepository regionRepository;
    private final MembershipRepository membershipRepository;
    private final PostRepository postRepository;


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
     * @param userId   회원 아이디
     * @param regionId 지역 아이디
     * @param boardId  게시판 아이디
     * @param pageable 페이징 페이지, 크기
     * @return MainPostResponse
     */
    public MainPostResponse getPost(Long userId, Long regionId, Long boardId, Long lastPostId,
        Pageable pageable) {
        log.info("Main enter");

        ListToStringConverter converter = new ListToStringConverter();

        // membership
        Membership membership = membershipRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        // 게시글 좋아요 여부
        List<String> likesList = membership.getLikelist() == null ? null : membership.getLikelist();

        log.info("membership.getGender(): " + membership.getGender());

        // 게시글 리스트
        List<PostProjection> postProjectionList =
            postRepository.findMainResponse(regionId, boardId, membership.getGender(), lastPostId,
                pageable);

        List<PostProjectionMainDto> postProjectionMainDtoList = new ArrayList<>();

        for (PostProjection projection : postProjectionList) {

            // 날짜
            LocalDateTime localDateTime = projection.getCreatedAt();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateTime = localDateTime.format(formatter);

            PostProjectionMainDto postProjectionMainDto = PostProjectionMainDto.builder()
                .boardId(projection.getBoardId())
                .postId(projection.getPostId())
                .regionId(projection.getRegionId())
                .address(projection.getAddress())
                .content(projection.getContent())
                .createdAt(formattedDateTime)
                .memberId(projection.getMemberId() == null ? null : projection.getMemberId())
                .nickName(projection.getNickName() == null ? "" : projection.getNickName())
                .gender(projection.getGender() == null ? null : projection.getGender())
                .userFile(projection.getUserFile())
                .commentCount(projection.getCommentCount())
                .likesFlag(
                    likesList != null && likesList.contains(String.valueOf(projection.getPostId()))
                )
                .roomFiles(
                    converter.convertToEntityAttribute(projection.getRoomFiles())
                )
                .build();

            postProjectionMainDtoList.add(postProjectionMainDto);
        }

        // 게시글의 마지막 id
        Long newLastPostId =
            postProjectionList.size() == 0 ? null
                : postProjectionList.get(postProjectionList.size() - 1).getPostId();

        return MainPostResponse.of(postProjectionMainDtoList, newLastPostId);
    }
}
