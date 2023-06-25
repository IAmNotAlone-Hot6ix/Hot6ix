package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.LikesListPostProjectionDto;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.LikesListPostResponse;
import com.hotsix.iAmNotAlone.domain.membership.model.dto.LikesPostProjection;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
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
public class MembershipLikesListService {

    private final MembershipRepository membershipRepository;
    private final PostRepository postRepository;


    /**
     * 좋아요 한 게시글 조회
     */
    public LikesListPostResponse getPost(Long memberId, Long lastPostId, Pageable pageable) {
        log.info("member likes List enter");

        ListToStringConverter converter = new ListToStringConverter();

        // membership
        Membership membership = membershipRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        // member 의 좋아요한 게시글
        List<Long> likesList = membership.getLikelist() == null ? null : membership.getLikelist()
            .stream()
            .filter(s -> !s.isEmpty())
            .map(Long::valueOf)
            .collect(Collectors.toList());

        // 조회해온 좋아요 게시글 리스트
        List<LikesPostProjection> postProjectionList =
            postRepository.findLikesListResponse(likesList, lastPostId, pageable);

        List<LikesListPostProjectionDto> likesListPostProjectionDtoList = new ArrayList<>();

        for (LikesPostProjection likesPostProjection : postProjectionList) {

            // 날짜
            LocalDateTime localDateTime = likesPostProjection.getCreatedAt();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateTime = localDateTime.format(formatter);

            LikesListPostProjectionDto postProjectionMainDto = LikesListPostProjectionDto.builder()
                .boardId(likesPostProjection.getBoardId())
                .postId(likesPostProjection.getPostId())
                .regionId(likesPostProjection.getRegionId())
                .address(likesPostProjection.getAddress())
                .content(likesPostProjection.getContent())
                .createdAt(formattedDateTime)
                .memberId(likesPostProjection.getMemberId() == null ?
                    null : likesPostProjection.getMemberId())
                .nickName(likesPostProjection.getNickName() == null ?
                    "" : likesPostProjection.getNickName())
                .gender(likesPostProjection.getGender() == null ?
                    null : likesPostProjection.getGender())
                .userFile(likesPostProjection.getUserFile())
                .commentCount(likesPostProjection.getCommentCount())
                .likesFlag(true)
                .roomFiles(
                    converter.convertToEntityAttribute(likesPostProjection.getRoomFiles())
                )
                .build();

            likesListPostProjectionDtoList.add(postProjectionMainDto);
        }

        // 게시글의 마지막 id
        Long newLastPostId =
            postProjectionList.size() == 0 ? null
                : postProjectionList.get(postProjectionList.size() - 1).getPostId();

        log.info("member likes List end");
        return LikesListPostResponse.of(likesListPostProjectionDtoList, newLastPostId);
    }
}
