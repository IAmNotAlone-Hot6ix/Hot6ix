package com.hotsix.iAmNotAlone.domain.post.repository;

import com.hotsix.iAmNotAlone.domain.membership.model.dto.LikesPostProjection;
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

    // 마이페이지 무한스크롤
    Page<Post> findByIdLessThanAndMembershipIdOrderByIdDesc(Long lastPostId, Long membershipId, PageRequest pageRequest);

    // 마이페이지 게시글 세팅
    List<Post> findTop10ByMembershipIdOrderByIdDesc(Long loginMemberId);

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


    @Query("    SELECT p.boardId                    AS boardId"
            + "      , p.id                         AS postId"
            + "      , p.regionId                   AS regionId"
            + "      , p.address                    AS address"
            + "      , p.content                    AS content"
            + "      , p.createdAt                  AS createdAt"
            + "      , m.id                         AS memberId"
            + "      , m.nickname                   AS nickName"
            + "      , m.gender                     AS gender"
            + "      , m.imgPath                    AS userFile"
            + "      , p.imgPath                    AS roomFiles "
            + "      , count(c)                     AS commentCount"
            + "   FROM Post             p "
        + "  LEFT JOIN p.membership     m"
        + "  LEFT JOIN Comments         c "
            + "     ON p.id             = c.post.id "
        + "      WHERE p.id             IN :memberLikePostList"
        + "        AND (:lastPostId IS NULL "
        +"                  OR p.id < :lastPostId)"
        + "   GROUP BY p.id")
    List<LikesPostProjection> findLikesListResponse(List<Long> memberLikePostList, Long lastPostId,
        Pageable pageable);
}
