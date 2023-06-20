package com.hotsix.iAmNotAlone.domain.likes.repository;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LikesRepository {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public LikesRepository(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    public void addLike(String key, String userId) {
        stringRedisTemplate.opsForSet().add(key, userId);
    }

    public void removeLike(String key, String userId) {
        stringRedisTemplate.opsForSet().remove(key, userId);
    }

    public boolean isLiked(String key, String userId) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, userId));
    }

    public Long getLikeCount(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    public Set<String> getSetValues(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }
}
