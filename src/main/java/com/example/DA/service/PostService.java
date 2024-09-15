package com.example.DA.service;

import com.example.DA.dto.PostDTO;
import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PostWithPropertyDTO;
import com.example.DA.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    List<PostDTO> getAllPosts();

    PostDTO getPostById(Integer postId);

    PostDTO createPost(PostDTO postDTO);

    PostDTO updatePost(Integer postId, PostDTO postDTO);

    void deletePost(Integer postId);

    public Page<Post> searchPost(PostSearchCriteria postSearchCriteria, Pageable pageable);

    public List<PostDTO> getFavoritePost(Integer userId);

    public PostWithPropertyDTO getPostWithProperty(Integer postId);
}
