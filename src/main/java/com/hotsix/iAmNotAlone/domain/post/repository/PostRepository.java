package com.hotsix.iAmNotAlone.domain.post.repository;

import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 마이페이지 무한스크롤
    Page<Post> findByIdLessThanAndMembershipIdOrderByIdDesc(Long lastPostId, Long membershipId, PageRequest pageRequest);

    // 마이페이지 게시글 세팅
    List<Post> findTop10ByMembershipIdOrderByIdDesc(Long loginMemberId);

    @Override
    @EntityGraph(attributePaths = {"membership"})
    Optional<Post> findById(Long postId);

    @Modifying
    @Query("SELECT p from Post p where p.membership.id = :membershipId")
    List<Post> findAllByMembershipId(@Param("membershipId") Long id);

    @Modifying
    @Query("DELETE from Post p where p.membership.id = :membershipId")
    void deleteAllByMembershipId(@Param("membershipId") Long id);


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
