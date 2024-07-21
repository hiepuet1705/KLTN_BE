package com.example.DA.service;

import com.example.DA.dto.PostDTO;

import java.util.List;

public interface PostService {
    List<PostDTO> getAllPosts();

    PostDTO getPostById(Integer postId);

    PostDTO createPost(PostDTO postDTO);

    PostDTO updatePost(Integer postId, PostDTO postDTO);

    void deletePost(Integer postId);
}
