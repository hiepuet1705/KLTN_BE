package com.example.DA.service.imp;

import com.example.DA.dto.PostDTO;
import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PostWithPropertyDTO;
import com.example.DA.dto.PropertyDTO;

import com.example.DA.model.Post;
import com.example.DA.model.Property;
import com.example.DA.model.enums_entity.District;
import com.example.DA.model.enums_entity.Province;
import com.example.DA.repo.*;
import com.example.DA.service.PostService;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private ModelMapper modelMapper;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private PropertyServiceImpl propertyService;


    @Override
    public List<PostWithPropertyDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToPostWithPropertyDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getPostByUserId(Integer userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        // Convert Post entities to PostDTOs
        return posts.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public PostDTO getPostById(Integer postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.map(this::convertToDTO).orElse(null);
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Post post = convertToEntity(postDTO);
        post = postRepository.save(post);
        return convertToDTO(post);
    }

    @Override
    public PostDTO updatePost(Integer postId, PostDTO postDTO) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            modelMapper.map(postDTO, post);
            post.setProperty(propertyRepository.findById(postDTO.getPropertyId()).orElse(null));
            post.setUser(userRepository.findById(postDTO.getUserId()).orElse(null));
            post = postRepository.save(post);
            return convertToDTO(post);
        }
        return null;
    }

    @Override
    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Page<PostWithPropertyDTO> searchPost(PostSearchCriteria criteria, Pageable pageable) {
        String province = criteria.getProvince();
        Integer provinceId = Optional.ofNullable(provinceRepository.findByName(province))
                .map(Province::getId)
                .orElse(null);

        Integer districtId = Optional.ofNullable(districtRepository.findByName(criteria.getDistrict()))
                .map(District::getId)
                .orElse(null);

        Page<Post> posts = postRepository.searchPosts(
                districtId,
                provinceId,
                criteria.getMinPrice(),
                criteria.getMaxPrice(),
                criteria.getPostType(),
                criteria.getMinArea(),
                criteria.getMaxArea(),
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

        PropertyDTO propertyDTO = propertyService.convertToDTO(post.getProperty());
        dto.setProperty(propertyDTO);
        dto.setUserId(post.getUser().getId());

        return dto;
    }

    @Override
    public List<PostDTO> getFavoritePost(Integer userId) {

        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());


    }

    @Override
    public PostWithPropertyDTO getPostWithProperty(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Property property = post.getProperty();
        PropertyDTO propertyDTO = new PropertyDTO(
                property.getPropertyId(),
                property.getStatus().getStatusId(),
                property.getOwner().getId(),
                property.getTitle(),
                property.getDescription(),
                property.getPrice(),
                property.getCategory().getCategoryId(),
                property.getLocation(),         // Thêm location
                property.getPhuong().getName(),           // Thêm phuong
                property.getDistrict().getName(),         // Thêm district
                property.getProvince().getName(),         // Thêm province
                property.getArea(),             // Thêm area
                property.getSophong(),          // Thêm số phòng
                property.getSoTang(),           // Thêm số tầng
                property.getSoToilet(),         // Thêm số toilet
                property.getLat(),              // Thêm tọa độ lat
                property.getLon(),              // Thêm tọa độ lon
                property.getAge()               // Thêm tuổi của bất động sản
        );
        PostWithPropertyDTO postDTO = new PostWithPropertyDTO(
                post.getPostId(),
                post.getPostTitle(),
                post.getPostContent(),
                post.getCharged(),
                post.getPrice(),
                post.getStatus(),
                post.getPostType(),
                propertyDTO,  // Đưa PropertyDTO vào PostDTO
                post.getUser().getId()
        );

        return postDTO;
    }

    @Override
    public List<PostDTO> getPostsByStatus(String status) {
        List<Post> posts = postRepository.findByStatus(status);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO updatePostStatus(Integer postId, String status) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        // Validate the new status
        if (!status.matches("pending|approved|rejected")) {
            throw new IllegalArgumentException("Invalid status. Status must be 'pending', 'approved', or 'rejected'.");
        }

        // Update status
        post.setStatus(status);
        postRepository.save(post);

        return convertToDTO(post);
    }


    private PostDTO convertToDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }

    private Post convertToEntity(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        post.setProperty(propertyRepository.findById(postDTO.getPropertyId()).orElse(null));
        post.setUser(userRepository.findById(postDTO.getUserId()).orElse(null));
        return post;
    }
}
