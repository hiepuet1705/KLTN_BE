package com.example.DA.service.imp;

import com.example.DA.dto.PostDTO;
import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PostWithPropertyDTO;
import com.example.DA.dto.PropertyDTO;
import com.example.DA.model.Post;
import com.example.DA.model.Property;
import com.example.DA.repo.PostRepository;
import com.example.DA.repo.PropertyRepository;
import com.example.DA.repo.UserRepository;
import com.example.DA.service.PostService;
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


    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
    public Page<Post> searchPost(PostSearchCriteria criteria, Pageable pageable) {
        return postRepository.searchPosts(
                criteria.getDistrict(),
                criteria.getProvince(),
                criteria.getMinPrice(),
                criteria.getMaxPrice(),
                criteria.getStatus(),
                criteria.getMinArea(),
                criteria.getMaxArea(),
                pageable);
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
