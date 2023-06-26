package com.hotsix.iAmNotAlone.domain.post.repository;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByIdLessThanAndMembershipIdOrderByIdDesc(Long lastPostId, Long membershipId, PageRequest pageRequest);

    List<Post> findTop5ByMembershipIdOrderByIdDesc(Long loginMemberId);

    @Override
    @EntityGraph(attributePaths = {"membership"})
    Optional<Post> findById(Long postId);


    @EntityGraph(attributePaths = {"membership"})
    @Query("    SELECT p "
            + "   FROM Post             p "
        + "      WHERE p.regionId       = :regionId "
        + "        AND p.boardId        = :boardId "
        + "        AND (:lastPostId IS NULL OR p.id < :lastPostId)")
    List<Post> findRecentPostsByRegionAndBoard(Long regionId, Long boardId, Long lastPostId,
        Pageable pageable);


    @EntityGraph(attributePaths = {"membership"})
    @Query("    SELECT p "
        + "   FROM Post             p "
        + "      WHERE p.id             IN :memberLikePostList"
        + "        AND (:lastPostId IS NULL OR p.id < :lastPostId)")
    List<Post> findRecentPostsByLikeList(List<Long> memberLikePostList, Long lastPostId,
        Pageable pageable);
}
