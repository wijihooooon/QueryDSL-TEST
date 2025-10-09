package com.seungmin.homework.domain.post.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("게시물 - 태그")
public class PostTag {

    @EmbeddedId
    private PostTagId id;

    @Getter
    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    public class PostTagId{
        @JoinColumn(name = "post_id", nullable = false)
        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private Post postId;

        @JoinColumn(name = "tag_id", nullable = false)
        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private Post tagId;
    }
}
