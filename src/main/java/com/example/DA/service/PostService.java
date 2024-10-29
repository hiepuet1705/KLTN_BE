package com.example.DA.service;

import com.example.DA.dto.PostDTO;
import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PostWithPropertyDTO;
import com.example.DA.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Integer countApprovedPostsByPropertyId(Integer propertyId);

    List<PostWithPropertyDTO> getAllPosts();

    List<PostWithPropertyDTO> getPostByPropertyId(Integer propertyId);

    List<PostWithPropertyDTO> getPostByUserId(Integer userId);

    PostWithPropertyDTO getPostById(Integer postId);

    PostWithPropertyDTO createPost(PostDTO postDTO);

//    PostDTO updatePost(Integer postId, PostDTO postDTO);

    void deletePost(Integer postId);

    public Page<PostWithPropertyDTO> searchPost(PostSearchCriteria postSearchCriteria, Pageable pageable);

//    public List<PostDTO> getFavoritePost(Integer userId);

    public PostWithPropertyDTO getPostWithProperty(Integer postId);

    public List<PostWithPropertyDTO> getPostsByStatus(String status);

    public String updatePaymentStatus(Integer postId);


    public List<PostWithPropertyDTO> getFavouritePosts(Integer userId);

    public PostWithPropertyDTO updatePostStatus(Integer postId, String status);

    void checkPostExpiration();
}
