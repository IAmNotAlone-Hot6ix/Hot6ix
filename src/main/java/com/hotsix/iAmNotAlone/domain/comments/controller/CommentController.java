package com.hotsix.iAmNotAlone.domain.comments.controller;

import com.hotsix.iAmNotAlone.domain.comments.model.dto.CommentsDetailResponseDto;
import com.hotsix.iAmNotAlone.domain.comments.model.dto.CommentsUpdateRemoveResponseDto;
import com.hotsix.iAmNotAlone.domain.comments.model.form.CommentRequestForm;
import com.hotsix.iAmNotAlone.domain.comments.model.form.CommentUpdateRequestForm;
import com.hotsix.iAmNotAlone.domain.comments.service.CommentModifyService;
import com.hotsix.iAmNotAlone.domain.comments.service.CommentPageService;
import com.hotsix.iAmNotAlone.domain.comments.service.CommentRemoveService;
import com.hotsix.iAmNotAlone.domain.comments.service.CommentsRegisterService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentsRegisterService commentsRegisterService;
    private final CommentPageService commentPageService;
    private final CommentModifyService commentModifyService;
    private final CommentRemoveService commentRemoveService;

    // 댓글 등록 api
    @PostMapping("/api/comment/{postId}/{userId}")
    public ResponseEntity<CommentsDetailResponseDto> commentAdd(@PathVariable Long postId, @PathVariable Long userId, @RequestBody CommentRequestForm form) {
        CommentsDetailResponseDto responseDto = commentsRegisterService.addComments(postId, userId, form);

        return ResponseEntity.ok(responseDto);
    }

    //댓글 수정 api
    @PutMapping("/api/comment/{commentId}")
    public ResponseEntity<CommentsUpdateRemoveResponseDto> commentModify(@PathVariable Long commentId,@RequestBody CommentUpdateRequestForm form){
        return ResponseEntity.ok(commentModifyService.modifyComment(commentId, form));
    }

    //댓글 삭제 api
    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<CommentsUpdateRemoveResponseDto> commentDelete(@PathVariable Long commentId){
        return ResponseEntity.ok(commentRemoveService.removeComment(commentId));
    }

    // 댓글 무한스크롤 요청 api
    @GetMapping("/api/comment/{postId}")
    public ResponseEntity<Result> getCommentLowerThanId(@RequestParam Long lastCommentId,
                                                                    @RequestParam int size, @PathVariable Long postId) {
        List<CommentsDetailResponseDto> commentResponse = commentPageService.commentPagesBy(lastCommentId, size, postId);
        return ResponseEntity.ok(new Result(commentResponse));
    }

    // 상세페이지 댓글 세팅 api
    @GetMapping("/api/comment/basic/{postId}")
    public ResponseEntity<Result> getCommentInUserId(@PathVariable Long postId) {
        List<CommentsDetailResponseDto> commentsDetailResponseDtoList = commentPageService.commentBasicSetting(postId);
        return ResponseEntity.ok(new Result<>(commentsDetailResponseDtoList));
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
