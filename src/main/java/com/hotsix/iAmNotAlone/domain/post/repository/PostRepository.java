package com.hotsix.iAmNotAlone.domain.post.repository;

import com.hotsix.iAmNotAlone.domain.main.model.dto.PostProjection;
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

    Page<Post> findByIdLessThanAndMembershipIdOrderByIdDesc(Long lastPostId, Long membershipId, PageRequest pageRequest);

    List<Post> findTop5ByMembershipIdOrderByIdDesc(Long loginMemberId);

    @Override
    @EntityGraph(attributePaths = {"membership"})
    Optional<Post> findById(Long postI7);


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
        + "      WHERE p.regionId       = :regionId "
        + "        AND p.boardId        = :boardId "
        + "        AND p.gender         = :gender "
        + "        AND (:lastPostId IS NULL "
        +"                  OR p.id < :lastPostId)"
        + "   GROUP BY p.id")
    List<PostProjection> findMainResponse(Long regionId, Long boardId, int gender, Long lastPostId,
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
