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
    @Query("SELECT p FROM Post p JOIN p.property prop WHERE " +
            "(:district IS NULL OR prop.district = :district) AND " +
            "(:province IS NULL OR prop.province = :province) AND " +
            "(:minPrice IS NULL OR prop.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR prop.price <= :maxPrice) AND " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:minArea IS NULL OR prop.area >= :minArea) AND " +
            "(:maxArea IS NULL OR prop.area <= :maxArea) " +
            "ORDER BY p.charged DESC, prop.price ASC")
    Page<Post> searchPosts(
            @Param("district") String district,
            @Param("province") String province,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("status") String status,
            @Param("minArea") Double minArea,
            @Param("maxArea") Double maxArea,
            Pageable pageable);


}
