package com.hotsix.iAmNotAlone.domain.post.repository;

import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByIdLessThanAndMembershipInOrderByIdDesc(Long lastPostId, List<Membership> membership, PageRequest pageRequest);

    List<Post> findTop5ByMembershipInOrderByIdDesc(List<Membership> membershipList);


}
