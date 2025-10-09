package com.seungmin.homework.domain.post.repository;

import com.seungmin.homework.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = """
        SELECT 
            m.name AS memberName,
            p.title AS title,
            p.content AS content,
            p.created_at AS createdAt,
            GROUP_CONCAT(t.name ORDER BY t.name SEPARATOR ',') AS tagName
        FROM post p
        JOIN member m ON p.member_id = m.id
        LEFT JOIN post_tag pt ON p.id = pt.post_id
        LEFT JOIN tag t ON pt.tag_id = t.id
        GROUP BY p.id
        ORDER BY p.created_at DESC
        """,
            countQuery = """
        SELECT COUNT(*) 
        FROM post p
        """,
            nativeQuery = true)
    Page<Object[]> findPagedPosts(Pageable pageable);
}
