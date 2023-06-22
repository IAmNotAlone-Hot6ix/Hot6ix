package com.hotsix.iAmNotAlone.domain.post.entity;

import com.hotsix.iAmNotAlone.domain.comments.entity.Comments;
import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.post.model.form.AddPostForm;
import com.hotsix.iAmNotAlone.domain.post.model.form.ModifyPostForm;
import com.hotsix.iAmNotAlone.global.util.ListToStringConverter;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Membership membership;

    @Column(name = "region_id")
    private Long regionId;

    @Column(length = 50)
    private String address;

    @Column(length = 3000)
    private String content;

    @Column
    private Long likes;

    @Column
    private int gender;

    @Convert(converter = ListToStringConverter.class)
    @Column(name = "img_path")
    private List<String> imgPath;

    @Builder.Default
    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE)
    private List<Comments> commentsList = new ArrayList<>();

    public static Post createPost(AddPostForm form, Membership membership, List<String> path) {
        return Post.builder()
            .boardId(form.getBoardId())
            .membership(membership)
            .regionId(form.getRegionId())
            .content(form.getContent())
            .address(form.getAddress())
            .likes(0L)
            .gender(membership.getGender())
            .imgPath(path)
            .build();
    }

    public void modifyPost(ModifyPostForm form) {
        this.boardId = form.getBoardId();
        this.regionId = form.getRegionId();
        this.address = form.getAddress();
        this.content = form.getContent();
        this.imgPath = form.getImgPath();
    }

    // 좋아요 수 업데이트
    public void updateLikes(Long likes) {
        this.likes = likes;
    }

}
