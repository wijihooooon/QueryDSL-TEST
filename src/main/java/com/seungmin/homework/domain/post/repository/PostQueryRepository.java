package com.seungmin.homework.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seungmin.homework.domain.post.dto.res.PostRes;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;


//    public static Page<PostRes> getPageList(int pageNumber, int pageSize) {
//    }
//
//    private Optional<PostRes> findPageList(int pageNumber, int pageSize) {
//        return Optional.ofNullable(
//            jpaQueryFactory
//                .select(new QPostRes)
//                .from()
//                .where()
//        )
//    }
}
