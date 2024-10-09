package com.example.DA.service.imp;

import com.example.DA.dto.PostDTO;
import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PostWithPropertyDTO;


import com.example.DA.dto.PropertyDTOResponse;
import com.example.DA.model.FavoriteList;
import com.example.DA.model.Post;

import com.example.DA.model.enums_entity.Category;

import com.example.DA.repo.*;
import com.example.DA.service.PostService;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private FavoriteListRepository favoriteListRepository;

    @Autowired
    private PropertyServiceImpl propertyService;


    @Override
    public List<PostWithPropertyDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToPostWithPropertyDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostWithPropertyDTO> getPostByUserId(Integer userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        // Convert Post entities to PostDTOs
        return posts.stream()
                .map(this::convertToPostWithPropertyDTO)
                .toList();
    }

    @Override
    public PostWithPropertyDTO getPostById(Integer postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.map(this::convertToPostWithPropertyDTO).orElse(null);
    }

    @Override
    public PostWithPropertyDTO createPost(PostDTO postDTO) {
        Post post = convertToEntity(postDTO);
        post = postRepository.save(post);
        return convertToPostWithPropertyDTO(post);
    }

//    @Override
//    public PostDTO updatePost(Integer postId, PostDTO postDTO) {
//        Optional<Post> optionalPost = postRepository.findById(postId);
//        if (optionalPost.isPresent()) {
//            Post post = optionalPost.get();
//            modelMapper.map(postDTO, post);
//            post.setProperty(propertyRepository.findById(postDTO.getPropertyId()).orElse(null));
//            post.setUser(userRepository.findById(postDTO.getUserId()).orElse(null));
//            post = postRepository.save(post);
//            return convertToDTO(post);
//        }
//        return null;
//    }

    @Override
    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Page<PostWithPropertyDTO> searchPost(PostSearchCriteria criteria, Pageable pageable) {
        String province = criteria.getProvince();

        Integer provinceId = Optional.ofNullable(provinceRepository.findByName(province))
                .filter(provinces -> !provinces.isEmpty()) // Kiểm tra nếu mảng không rỗng
                .map(provinces -> provinces.get(0).getId()) // Lấy giá trị đầu tiên
                .orElse(null);

        Integer districtId = Optional.ofNullable(districtRepository.findByName(criteria.getDistrict()))
                .filter(districts -> !districts.isEmpty()) // Kiểm tra nếu mảng không rỗng
                .map(districts -> districts.get(0).getId())
                .orElse(null);// Lấy giá trị đầu tiên


        Integer categoryId = Optional.ofNullable(categoryRepository.findByName(criteria.getPropertyType()))
                .map(Category::getCategoryId)
                .orElse(null);

        Page<Post> posts = postRepository.searchPosts(
                districtId,
                provinceId,
                criteria.getMinPrice(),
                criteria.getMaxPrice(),
                criteria.getPostType(),
                criteria.getMinArea(),
                criteria.getMaxArea(),
                categoryId,
                pageable);

        return posts.map(this::convertToPostWithPropertyDTO);

    }

    private PostWithPropertyDTO convertToPostWithPropertyDTO(Post post) {
        PostWithPropertyDTO dto = new PostWithPropertyDTO();
        dto.setPostId(post.getPostId());
        dto.setPostTitle(post.getPostTitle());
        dto.setPostContent(post.getPostContent());
        dto.setCharged(post.getCharged());
        dto.setPrice(post.getPrice());
        dto.setStatus(post.getStatus());
        dto.setPostType(post.getPostType());

        PropertyDTOResponse propertyDTO = propertyService.convertToDTO(post.getProperty());
        dto.setProperty(propertyDTO);
        dto.setUserId(post.getUser().getId());

        return dto;
    }

//    @Override
//    public List<PostDTO> getFavoritePost(Integer userId) {
//
//        List<Post> posts = postRepository.findAll();
//
//
//
//    }

    @Override
    public PostWithPropertyDTO getPostWithProperty(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        PostWithPropertyDTO postDTO = convertToPostWithPropertyDTO(post);


        return postDTO;
    }


    @Override
    public List<PostWithPropertyDTO> getPostsByStatus(String status) {
        List<Post> posts = postRepository.findByStatus(status);
        return posts.stream()
                .map(this::convertToPostWithPropertyDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<PostWithPropertyDTO> getFavouritePosts(Integer userId) {
        List<FavoriteList> favorites = favoriteListRepository.findByUser_UserId(userId);
        List<PostWithPropertyDTO> favoritePosts = favorites.stream()
                .map(favorite -> {
                    Optional<Post> post = postRepository.findById(favorite.getPost().getPostId());

                    // Chỉ chuyển đổi nếu post tồn tại và có status là "approved"
                    return post.filter(p -> "approved".equalsIgnoreCase(p.getStatus()))
                            .map(this::convertToPostWithPropertyDTO)
                            .orElse(null);
                })
                .filter(Objects::nonNull)  // Loại bỏ các giá trị null
                .collect(Collectors.toList());

        return favoritePosts;
    }

    @Override
    public PostWithPropertyDTO updatePostStatus(Integer postId, String status) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        // Validate the new status
        if (!status.matches("pending|approved|rejected")) {
            throw new IllegalArgumentException("Invalid status. Status must be 'pending', 'approved', or 'rejected'.");
        }

        // Update status
        post.setStatus(status);
        postRepository.save(post);

        return convertToPostWithPropertyDTO(post);
    }


    private Post convertToEntity(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        post.setProperty(propertyRepository.findById(postDTO.getPropertyId()).orElse(null));
        post.setUser(userRepository.findById(postDTO.getUserId()).orElse(null));
        return post;
    }
}
