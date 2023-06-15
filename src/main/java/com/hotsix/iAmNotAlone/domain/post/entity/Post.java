package com.hotsix.iAmNotAlone.domain.post.entity;

import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.domain.membership.entity.Membership;
import com.hotsix.iAmNotAlone.domain.post.model.form.AddPostForm;
import com.hotsix.iAmNotAlone.domain.region.entity.Region;
import com.hotsix.iAmNotAlone.global.util.ListToStringConverter;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
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
    private List<String> img_path;


    public static Post createPost(AddPostForm form, Membership membership, List<String> path) {
        return Post.builder()
            .boardId(form.getBoardId())
            .membership(membership)
            .regionId(form.getRegionId())
            .content(form.getContent())
            .address(form.getAddress())
            .likes(0L)
            .gender(form.getGender())
            .img_path(path)
            .build();
    }


}
