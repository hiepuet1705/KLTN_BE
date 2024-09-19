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
    @Query("SELECT p \n" +
            "FROM Post p \n" +
            "JOIN p.property prop \n" +
            "WHERE \n" +
            "    (:district IS NULL OR prop.district = :district) AND\n" +
            "    (:province IS NULL OR prop.province.id = :province) AND\n" +
            "    (:minPrice IS NULL OR prop.price >= :minPrice) AND\n" +
            "    (:maxPrice IS NULL OR prop.price <= :maxPrice) AND\n" +
            "    (:status IS NULL OR p.status = :status) AND\n" +
            "    (:minArea IS NULL OR prop.area >= :minArea) AND\n" +
            "    (:maxArea IS NULL OR prop.area <= :maxArea)\n" +
            "ORDER BY p.charged DESC, prop.price ASC\n")
    Page<Post> searchPosts(
            @Param("district") String district,
            @Param("province") Integer province,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("status") String status,
            @Param("minArea") Double minArea,
            @Param("maxArea") Double maxArea,
            Pageable pageable);


}
