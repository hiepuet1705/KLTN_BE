package com.example.DA.repo;

import com.example.DA.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByStatus(String status);

    @Query("SELECT p \n" +
            "FROM Post p \n" +
            "JOIN p.property prop \n" +
            "WHERE \n" +
            "    (:district IS NULL OR prop.district.id = :district) AND\n" +
            "    (:province IS NULL OR prop.province.id = :province) AND\n" +
            "    (:minPrice IS NULL OR p.price >= :minPrice) AND\n" +
            "    (:maxPrice IS NULL OR p.price <= :maxPrice) AND\n" +
            "    (:postType IS NULL OR :postType = '' OR p.postType = :postType) AND\n" +
            "    (:minArea IS NULL OR prop.area >= :minArea) AND\n" +
            "    (:maxArea IS NULL OR prop.area <= :maxArea) AND\n" +
            "    (:categoryId IS NULL OR prop.category.id = :categoryId)\n" +
            "ORDER BY p.charged DESC, prop.price ASC\n")
    Page<Post> searchPosts(
            @Param("district") Integer district,
            @Param("province") Integer province,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("postType") String postType,
            @Param("minArea") Double minArea,
            @Param("maxArea") Double maxArea,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);


    List<Post> findByUserId(Integer userId);


}
