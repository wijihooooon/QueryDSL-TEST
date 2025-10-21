package com.seungmin.homework.domain.post.service;

import com.seungmin.homework.domain.post.dto.res.PostRes;
import com.seungmin.homework.domain.post.mapper.PostMapper;
import com.seungmin.homework.domain.post.repository.PostQueryRepository;
import com.seungmin.homework.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostMapper postMapper;

//    public Page<PostRes> getPost(int pageNumber) {
//        int pageSize = 3;
//        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
//
//        Page<Object[]> result = postRepository.findPagedPosts(pageable);
//
//        return result.map(postMapper::toPostRes);
//    }

    public Page<PostRes> getPost(int pageNumber){
        int pageSize = 3;

        Page<PostRes> result = PostQueryRepository.getPageList(pageNumber, pageSize);

        return result.map(postMapper::toPostRes);
    }
}