package com.example.DA.controller;

import com.example.DA.dto.PostDTO;
import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PostWithPropertyDTO;
import com.example.DA.dto.UpdatePostStatusDTO;
import com.example.DA.service.PostService;
import jakarta.persistence.EntityNotFoundException;
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
        postService.checkPostExpiration();
        return postService.getPostWithProperty(id);
    }


    @GetMapping("/count/{propertyId}")
    public ResponseEntity<Integer> countApprovedPostsByPropertyId(@PathVariable Integer propertyId) {
        Integer count = postService.countApprovedPostsByPropertyId(propertyId);
        return ResponseEntity.ok(count);
    }


    @GetMapping("/properties/{propertyId}")
    public List<PostWithPropertyDTO> getPostByPropertyId(@PathVariable Integer propertyId) {
        postService.checkPostExpiration();
        return postService.getPostByPropertyId(propertyId);
    }


    @GetMapping
    public List<PostWithPropertyDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/search")
    public Page<PostWithPropertyDTO> searchPosts(@RequestBody PostSearchCriteria criteria, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        postService.checkPostExpiration();
        return postService.searchPost(criteria, pageable);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<List<PostWithPropertyDTO>> getPostsByUserId(@PathVariable Integer userId) {
        List<PostWithPropertyDTO> posts = postService.getPostByUserId(userId);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<List<PostWithPropertyDTO>> getPostsByStatus(@RequestParam(name = "status", required = false, defaultValue = "pending") String status) {
        postService.checkPostExpiration();
        List<PostWithPropertyDTO> posts = postService.getPostsByStatus(status);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getPostCountByMonth(
            @RequestParam Integer month,
            @RequestParam Integer year) {

        int postCount = postService.getPostByMonth(month, year); // Call your service method
        return ResponseEntity.ok(postCount);
    }

    @PutMapping("/{postId}/status")
    public ResponseEntity<PostWithPropertyDTO> updatePostStatus(@PathVariable Integer postId, @RequestBody UpdatePostStatusDTO updatePostStatusDTO) {
        try {
            PostWithPropertyDTO updatedPost = postService.updatePostStatus(postId, updatePostStatusDTO.getStatus());
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{postId}/payment-status")
    public ResponseEntity<String> updatePaymentStatus(@PathVariable Integer postId) {
        try {
            postService.updatePaymentStatus(postId); // Set paymentStatus thành 1
            return ResponseEntity.ok("Payment status updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public PostWithPropertyDTO createPost(@RequestBody PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<PostDTO> updatePost(@PathVariable Integer id, @RequestBody PostDTO postDTO) {
//        PostDTO updatedPost = postService.updatePost(id, postDTO);
//        return updatedPost != null ? ResponseEntity.ok(updatedPost) : ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}