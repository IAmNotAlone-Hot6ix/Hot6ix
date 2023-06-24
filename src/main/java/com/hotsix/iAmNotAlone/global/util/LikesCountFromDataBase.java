package com.hotsix.iAmNotAlone.global.util;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.Cursor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LikesCountFromDataBase {

    private final PostRepository postRepository;
    private final RedisUtil redisUtil;

    @Scheduled(fixedDelay = 600000) // 10분마다 실행
    public void migrateLikesCountToMySQL() {
        log.info("likes scheduled start!");
        Cursor<byte[]> cursor = redisUtil.getFindKeys("likes:*");

        while (cursor.hasNext()) {
            String key = new String(cursor.next());
            String postId = extractPostIdFromKey(key);
            Long likesCount = redisUtil.getLikeCount(key);

            if (likesCount != null) {
                try {
                    updatePostLikes(Long.valueOf(postId), likesCount);
                    log.info("post table likes update");

                } catch (Exception e) {
                    log.error("not found post: " + postId);

                } finally {
                    redisUtil.deleteData(key);
                    log.info("redis key delete: " + key);
                }
            }
        }
        log.info("likes scheduled end!");
    }

    private String extractPostIdFromKey(String key) {
        return key.split(":")[1];
    }

    @Transactional
    public void updatePostLikes(Long postId, Long likesCount) throws Exception {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception(""));
        post.updateLikes(likesCount);
        postRepository.save(post);
    }
}
