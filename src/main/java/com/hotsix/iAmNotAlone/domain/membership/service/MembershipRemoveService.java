package com.hotsix.iAmNotAlone.domain.membership.service;

import static com.hotsix.iAmNotAlone.global.exception.business.ErrorCode.NOT_FOUND_USER;

import com.hotsix.iAmNotAlone.domain.chat.service.ChatRoomRemoveService;
import com.hotsix.iAmNotAlone.domain.likes.service.LikesRemoveAllService;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.membership.repository.MembershipRepository;
import com.hotsix.iAmNotAlone.domain.post.service.PostRemoveService;
import com.hotsix.iAmNotAlone.global.exception.business.BusinessException;
import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipRemoveService {

    private final MembershipRepository membershipRepository;
    private final S3UploadService s3UploadService;
    private final PostRemoveService postRemoveService;
    private final LikesRemoveAllService likesRemoveAllService;
    private final ChatRoomRemoveService chatRoomRemoveService;

    /**
     * 회원 삭제
     */
    @Transactional
    public void removeMembership(Long id) {
        // 유저 정보에 이미지가 있으면 s3에 업로드된 이미지 삭제
        Membership membership = membershipRepository.findById(id).orElseThrow(
            () -> new BusinessException(NOT_FOUND_USER)
        );

        if (membership.getImgPath().length() != 0) {
            String[] sp = membership.getImgPath().split("\\.");

            // 카카오에서 가져온 이미지가 아니면 s3
            if (!sp[1].equals("kakaocdn")) {
                String[] split = membership.getImgPath().split("com/");
                s3UploadService.deleteFile(split[1]);
            }
        }

        // 유저가 좋아요한 게시글 count--
        likesRemoveAllService.deleteLikesByMemberId(id);

        //유저 성향 삭제

        
        // 유저가 작성한 게시글 삭제
        chatRoomRemoveService.removeChatRoom(id);
        postRemoveService.removeAllPost(id);
        membershipRepository.deleteById(id);
    }
}
