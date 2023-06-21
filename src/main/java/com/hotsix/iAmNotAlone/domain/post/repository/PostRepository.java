package com.hotsix.iAmNotAlone.domain.post.repository;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByIdLessThanAndMembershipIdOrderByIdDesc(Long lastPostId, Long membershipId, PageRequest pageRequest);

    List<Post> findTop5ByMembershipIdOrderByIdDesc(Long loginMemberId);


}
