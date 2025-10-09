package com.seungmin.homework.domain.post.dto.res;

import java.time.LocalDateTime;
import java.util.List;

public record PostRes(
        String memberName,
        String title,
        String content,
        LocalDateTime createdAt,
        List<String> tagName
) {
}
