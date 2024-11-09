package com.example.DA.repo;

import com.example.DA.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p WHERE p.status = :status AND p.postType = :purpose")
    List<Post> findByStatusAndPurpose(@Param("status") String status, @Param("purpose") String purpose);

    // Tìm bài đăng theo ID tỉnh, trạng thái và mục đích
    @Query("SELECT p FROM Post p WHERE p.property.province.id = :provinceId AND p.status = :status AND p.postType = :purpose")
    List<Post> findPostsByProvinceAndPurpose(@Param("provinceId") Integer provinceId, @Param("status") String status, @Param("purpose") String purpose);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.property.id = :propertyId AND p.status = 'approved'")
    Integer countApprovedPostsByPropertyId(@Param("propertyId") Integer propertyId);

    @Query("SELECT p FROM Post p WHERE p.property.propertyId = :propertyId AND p.status = 'approved'")
    List<Post> findByPropertyId(@Param("propertyId") Integer propertyId);


    List<Post> findByStatus(String status);

    @Query("SELECT p \n" +
            "FROM Post p \n" +
            "JOIN p.property prop \n" +
            "WHERE \n" +
            "    p.status = 'approved' AND\n" + // Thêm điều kiện mới
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
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("postType") String postType,
            @Param("minArea") Double minArea,
            @Param("maxArea") Double maxArea,
            @Param("categoryId") Integer categoryId,
            Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.status = 'approved'")
    List<Post> findApprovedPostsByUserId(@Param("userId") Integer userId);

    List<Post> findByUserId(Integer userId);

    @Query("""
                SELECT p FROM Post p
                WHERE p.postTitle = :postTitle
                  AND p.postContent = :postContent
                  AND p.charged = 1
                  AND p.price = :price
                  AND p.status = :status
                  AND p.postType = :postType
                  AND p.property.propertyId = :propertyId
                  AND p.user.id = :userId
            """)
    Optional<Post> findIfCharged(
            @Param("postTitle") String postTitle,
            @Param("postContent") String postContent,
            @Param("price") Long price,
            @Param("status") String status,
            @Param("postType") String postType,
            @Param("propertyId") Integer propertyId,
            @Param("userId") Integer userId
    );


}
