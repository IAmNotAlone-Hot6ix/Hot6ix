package com.hotsix.iAmNotAlone.domain.comments.repository;

import com.hotsix.iAmNotAlone.domain.comments.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {


    @Query(value = "select c from Comments c" +
            " join fetch c.membership m" +
            " where c.post.id = :postId")
    List<Comments> findByPost(@Param("postId") Long postId);

    List<Comments> findTop10ByPostIdOrderByIdAsc(Long postId);

    Page<Comments> findByIdGreaterThanAndPostIdOrderByIdAsc(Long lastCommentId, Long postId,
        PageRequest pageRequest);

    Long countByPostId(Long postId);
}
