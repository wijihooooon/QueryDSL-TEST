package com.seungmin.homework.domain.post.controller;

import com.seungmin.homework.domain.post.dto.res.PostRes;
import com.seungmin.homework.domain.post.service.PostService;
import com.seungmin.homework.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostRes>>> getPost(
            @RequestParam int pageNumber
    ){

        Page<PostRes> page = postService.getPost(pageNumber);
        return ApiResponse.ok(page);
    }
}
