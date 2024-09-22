package com.example.DA.controller;

import com.example.DA.dto.PostDTO;
import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PostWithPropertyDTO;
import com.example.DA.model.Post;
import com.example.DA.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public PostWithPropertyDTO getPostWithProperty(@PathVariable Integer id) {
        return postService.getPostWithProperty(id);
    }

    @GetMapping
    public List<PostWithPropertyDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/search")
    public Page<PostWithPropertyDTO> searchPosts(@RequestBody PostSearchCriteria criteria, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return postService.searchPost(criteria, pageable);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable Integer userId) {
        List<PostDTO> posts = postService.getPostByUserId(userId);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public PostDTO createPost(@RequestBody PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Integer id, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePost(id, postDTO);
        return updatedPost != null ? ResponseEntity.ok(updatedPost) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}