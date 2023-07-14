package com.hotsix.iAmNotAlone.domain.likes.repository;

import com.hotsix.iAmNotAlone.domain.likes.entity.Likes;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    Likes findByMemberIdAndPostId(Long memberId, Long postId);

    List<Likes> findByMemberId(Long memberId);
}
