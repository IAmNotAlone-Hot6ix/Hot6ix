//package com.hotsix.iAmNotAlone.domain.likes.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.IntStream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class LikesRegisterRemoveServiceTest {
//
//    @Autowired
//    private LikesRegisterRemoveService service;
//
//
//    @Test
//    @DisplayName("좋아요 테스트")
//    void addDeleteLike() throws InterruptedException {
//
//        final String postId = "2";  // 게시글 2
//        ExecutorService executorService = Executors.newFixedThreadPool(50);
//
//        IntStream.rangeClosed(1, 50).forEach(i -> {
//            executorService.submit(() -> {
//                String userId = String.valueOf(i + 7); // 로컬 DB의 유저 ID 8부터 시작하므로, i에 7을 더함
//                service.addDeleteLike(postId, userId);
//            });
//        });
//
//        executorService.shutdown();
//        executorService.awaitTermination(1, TimeUnit.MINUTES);
//
//        // 검증 로직. 예를 들면, 게시글의 좋아요 수가 50/0인지, 그리고 각 유저가 해당 게시글을 좋아하는지 확인
//        assertEquals(50, service.getLikeCount(postId));
////        assertEquals(0, service.getLikeCount(postId));
//
//        IntStream.rangeClosed(1, 50).forEach(i -> {
//            String userId = String.valueOf(i + 7);
//            assertTrue(service.isLiked(postId, userId));
//        });
//
//    }
//
//}