package com.hotsix.iAmNotAlone.global.util;

import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    // key를 통해 value 리턴
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    // 키 값으로 data가 있는지 확인
    public boolean existData(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // 유효 시간 동안 (key, value) 저장
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    // 유효 시간 없이 (key, value) 저장
    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void addConnect(String roomId) {
        redisTemplate.opsForValue().increment(roomId, 1);
    }

    public void deleteConnect(String roomId) {
        redisTemplate.opsForValue().decrement(roomId, 1);
    }

    // 삭제
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }


    // redis key 조합 (게시글 좋아요 count)
    public String getLikeKey(String postId) {
        return "likes:" + postId;
    }

    // count increment
    public void addLike(String key) {
        redisTemplate.opsForValue().increment(key, 1);
    }

    // count decrement
    public void removeLike(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    // findCount
    public Long getLikeCount(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0;
    }

    // findKeys
    public Cursor<byte[]> getFindKeys(String matchStr) {
        return Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()
            .scan(ScanOptions.scanOptions().match(matchStr).count(1000).build());
    }
}
