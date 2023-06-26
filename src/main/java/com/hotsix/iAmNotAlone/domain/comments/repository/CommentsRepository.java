package com.hotsix.iAmNotAlone.domain.comments.repository;

import com.hotsix.iAmNotAlone.domain.comments.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    @EntityGraph(attributePaths = {"membership"})
    List<Comments> findTop10ByPostIdOrderByIdAsc(Long postId);

    Page<Comments> findByIdGreaterThanAndPostIdOrderByIdAsc(Long lastCommentId, Long postId, PageRequest pageRequest);

    @Query("select count(c) from Comments c where c.post.id = :postId")
    Long countByPostId(@Param("postId") Long postId);

}
