package com.hotsix.iAmNotAlone.domain.board.post.repository;

import com.hotsix.iAmNotAlone.domain.board.post.entity.Post;
import com.hotsix.iAmNotAlone.domain.main.model.dto.PostProjection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("    SELECT p.board_id                   AS board_id"
            + "      , p.id                         AS post_id"
            + "      , p.region_id                  AS region_id"
            + "      , p.address                    AS address"
            + "      , p.content                    AS content"
            + "      , date_format(p.created_at, '%Y-%m-%d')   AS created_at"
            + "      , p.user_id                    AS user_id"
            + "      , m.nickname                   AS nick_name"
            + "      , m.gender                     AS gender"
            + "      , m.img_path                   AS user_file"
            + "      , (SELECT count(c.id)"
            + "           FROM Comments c"
            + "          WHERE p.id = c.post_id)    AS comment_count"
            + "      , m.likelist                   AS str_likes"
            + "      , p.img_path                   AS room_files "
            + "   FROM Post             p "
        + " INNER JOIN Membership       m "
            + "     ON p.user_id        = m.id "
        + "      WHERE p.region_id      = :regionId "
        + "        AND p.board_id       = :boardId "
        + "        AND m.gender         = :gender")
    List<PostProjection> findMainResponse(int gender, Long regionId, Long boardId, Pageable pageable);
}
