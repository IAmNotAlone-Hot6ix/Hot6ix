package com.hotsix.iAmNotAlone.domain.comments.entity;

import com.hotsix.iAmNotAlone.domain.comments.model.form.CommentRequestForm;
import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
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

    private void setPost(Post post) {
        if (this.post != null) { // 기존에 이미 팀이 존재한다면
            this.post.getCommentsList().remove(this); // 관계를 끊는다.
        }
        this.post = post;
        post.getCommentsList().add(this);
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
