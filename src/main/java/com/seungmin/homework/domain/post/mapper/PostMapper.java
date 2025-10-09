package com.seungmin.homework.domain.post.mapper;

import com.seungmin.homework.domain.post.dto.res.PostRes;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Component
public class PostMapper {

    public PostRes toPostRes(Object[] row) {
        String memberName = (String) row[0];
        String title = (String) row[1];
        String content = (String) row[2];
        Timestamp createdAt = (Timestamp) row[3];
        String tagConcat = (String) row[4];

        List<String> tags = tagConcat != null
                ? Arrays.stream(tagConcat.split(",")).toList()
                : List.of();

        return new PostRes(
                memberName,
                title,
                content,
                createdAt.toLocalDateTime(),
                tags
        );
    }
}
