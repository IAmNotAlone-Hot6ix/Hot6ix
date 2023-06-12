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
    @GetMapping("/{user_id}")
    public ResponseEntity<MainResponse> getMain(@PathVariable Long user_id) {
        return ResponseEntity.ok(mainService.getMain(user_id));
    }

    /**
     * 게시글 조회
     */
    @GetMapping("/{user_id}/{region_id}/{board_id}")
    public ResponseEntity<MainPostResponse> getPost(@PathVariable Long user_id,
        @PathVariable Long region_id, @PathVariable Long board_id, @RequestParam int page,
        @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        return ResponseEntity.ok(mainService.getPost(user_id, region_id, board_id, pageable));
    }
}
