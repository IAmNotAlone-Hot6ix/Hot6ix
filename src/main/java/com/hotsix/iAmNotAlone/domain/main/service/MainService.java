//package com.hotsix.iAmNotAlone.domain.main.service;
//
//import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;
//
//import com.hotsix.iAmNotAlone.domain.board.entity.Board;
//import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
//import com.hotsix.iAmNotAlone.domain.board.repository.BoardRepository;
//import com.hotsix.iAmNotAlone.domain.main.model.dto.BoardMainDto;
//import com.hotsix.iAmNotAlone.domain.main.model.dto.MainPostResponse;
//import com.hotsix.iAmNotAlone.domain.main.model.dto.MainResponse;
//import com.hotsix.iAmNotAlone.domain.main.model.dto.PostProjection;
//import com.hotsix.iAmNotAlone.domain.main.model.dto.PostProjectionMainDto;
//import com.hotsix.iAmNotAlone.domain.main.model.dto.RegionMainDto;
//import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
//import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
//import com.hotsix.iAmNotAlone.domain.region.entity.Region;
//import com.hotsix.iAmNotAlone.domain.region.repository.RegionRepository;
//import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
//import com.hotsix.iAmNotAlone.global.util.ListToStringConverter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class MainService {
//
//    private final RegionRepository regionRepository;
//    private final BoardRepository boardRepository;
//    private final MembershipRepository membershipRepository;
//    private final PostRepository postRepository;
//
//
//    /**
//     * 메인화면 컨트롤 데이터 바인딩용 조회
//     * @param userId 회원 아이디
//     * @return MainResponse
//     */
//    public MainResponse getMain(Long userId) {
//
//        // membership
//        Membership membership = membershipRepository.findById(userId)
//            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
//
//        // 지역 리스트
//        List<Region> regionList = regionRepository.findAll();
//
//        // 게시판 리스트
//        List<Board> boardList = boardRepository.findAll();
//
//        return MainResponse.of(
//            regionList.stream().map(RegionMainDto::from).collect(Collectors.toList()),
//            boardList.stream().map(BoardMainDto::from).collect(Collectors.toList()),
//            membership.getRegion().getId());
//    }
//
//    /**
//     * 게시글 조회
//     * @param userId   회원 아이디
//     * @param regionId 지역 아이디
//     * @param boardId  게시판 아이디
//     * @param pageable 페이징 페이지, 크기
//     * @return MainPostResponse
//     */
//    public MainPostResponse getPost(Long userId, Long regionId, Long boardId, Pageable pageable) {
//
//        ListToStringConverter converter = new ListToStringConverter();
//
//        // membership
//        Membership membership = membershipRepository.findById(userId)
//            .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
//
//        // 게시글 리스트
//        List<PostProjection> postProjectionList =
//            postRepository.findMainResponse(membership.getGender(), regionId, boardId, pageable);
//
//        List<PostProjectionMainDto> postProjectionMainDtoList = new ArrayList<>();
//
//        for (PostProjection projection : postProjectionList) {
//
//            // 게시글 좋아요 여부
//            List<String> likesList = converter.convertToEntityAttribute(projection.getStr_likes());
//
//            PostProjectionMainDto postProjectionMainDto = PostProjectionMainDto.builder()
//                .board_id(projection.getBoard_id())
//                .post_id(projection.getPost_id())
//                .region_id(projection.getRegion_id())
//                .address(projection.getAddress())
//                .content(projection.getContent())
//                .created_at(projection.getCreated_at())
//                .user_id(projection.getUser_id())
//                .nick_name(projection.getNick_name())
//                .gender(projection.getGender())
//                .user_file(projection.getUser_file())
//                .comment_count(projection.getComment_count())
//                .likes(likesList.contains(String.valueOf(projection.getPost_id())))
//                .room_files(converter.convertToEntityAttribute(projection.getRoom_files()))
//                .build();
//
//            postProjectionMainDtoList.add(postProjectionMainDto);
//        }
//
//        return MainPostResponse.from(postProjectionMainDtoList);
//    }
//}
