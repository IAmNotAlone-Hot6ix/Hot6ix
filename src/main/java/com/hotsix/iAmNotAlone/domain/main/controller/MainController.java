package com.hotsix.iAmNotAlone.domain.main.controller;

import com.hotsix.iAmNotAlone.domain.main.model.dto.MainPostResponse;
import com.hotsix.iAmNotAlone.domain.main.model.dto.MainResponse;
import com.hotsix.iAmNotAlone.domain.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {

    private final MainService mainService;


    /**
     * 메인화면 컨트롤 데이터 바인딩용 컨트롤러
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MainResponse> getMain(@PathVariable Long memberId) {
        return ResponseEntity.ok(mainService.getMain(memberId));
    }

    /**
     * 게시글 조회
     */
    @GetMapping("/{memberId}/{regionId}/{boardId}")
    public ResponseEntity<MainPostResponse> getPost(@PathVariable Long memberId,
        @PathVariable Long regionId, @PathVariable Long boardId,
        @RequestParam(required = false) Long lastPostId, @RequestParam int size) {

        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(
            mainService.getPost(memberId, regionId, boardId, lastPostId, pageable));
    }
}
