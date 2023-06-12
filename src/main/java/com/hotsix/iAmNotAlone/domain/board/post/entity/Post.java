package com.hotsix.iAmNotAlone.domain.board.post.entity;

import com.hotsix.iAmNotAlone.domain.common.BaseEntity;
import com.hotsix.iAmNotAlone.global.util.ListToStringConverter;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private Long board_id;

    private Long user_id;

    private Long region_id;

    @Column(length = 50)
    private String address;

    @Column(length = 3000)
    private String content;

    @Column
    private Long likes;

    @Convert(converter = ListToStringConverter.class)
    private List<String> img_path;
}
