package com.hotsix.iAmNotAlone.domain.comments.entity;

import com.hotsix.iAmNotAlone.domain.comments.model.form.CommentRequestForm;
import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comments extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Membership membership;

    @Column(length = 3000)
    private String content;

    public static Comments createComments(CommentRequestForm form, Membership membership, Post post) {
        return Comments.builder()
                .post(post)
                .membership(membership)
                .content(form.getContent())
                .build();
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
