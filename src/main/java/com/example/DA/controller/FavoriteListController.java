package com.example.DA.controller;

import com.example.DA.dto.AddFavoriteRequestDTO;
import com.example.DA.dto.PostWithPropertyDTO;
import com.example.DA.model.FavoriteList;
import com.example.DA.service.FavoriteListService;
import com.example.DA.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteListController {

    @Autowired
    private FavoriteListService favoriteListService;

    @Autowired
    private PostService postService;

    // API để thêm một bài viết yêu thích
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> addFavorite(@RequestBody AddFavoriteRequestDTO requestDTO) {
        Integer postId = favoriteListService.addFavorite(requestDTO);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

    // API để lấy danh sách bài viết yêu thích của người dùng dựa trên userId
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PostWithPropertyDTO>> getFavoritePosts(@PathVariable Integer userId) {
        List<PostWithPropertyDTO> favoritePosts = postService.getFavouritePosts(userId);
        return new ResponseEntity<>(favoritePosts, HttpStatus.OK);
    }

    // API kiểm tra kết nối hoặc xác thực
    @GetMapping("/check")
    public ResponseEntity<String> checkConnection() {
        return new ResponseEntity<>("Connection Successful!", HttpStatus.OK);
    }
}
