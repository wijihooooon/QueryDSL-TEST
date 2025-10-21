package com.seungmin.homework.domain.post.dto.res;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;

public record PostRes(
        String memberName,
        String title,
        String content,
        LocalDateTime createdAt,
        List<String> tagName
) {
    @QueryProjection
    public PostRes {}
}