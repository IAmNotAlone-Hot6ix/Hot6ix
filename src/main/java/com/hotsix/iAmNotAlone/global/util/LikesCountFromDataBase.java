package com.hotsix.iAmNotAlone.global.util;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.post.repository.PostRepository;
import java.util.Optional;
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
            String postId = key.split(":")[1];
            Long likesCountStr = redisUtil.getLikeCount(key);

            if (likesCountStr != null) {
                try {
                    updatePostLikes(Long.valueOf(postId), likesCountStr);
                    log.info("post table likes update");

                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("not found post: " + postId);

                } finally {
                    redisUtil.deleteData(key);
                    log.info("redis key delete: " + key);
                }
            }
        }
        log.info("likes scheduled end!");
    }

    @Transactional
    public void updatePostLikes(Long postId, Long likesCountStr) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.updateLikes(likesCountStr);
            postRepository.save(post);
        }
    }

}
