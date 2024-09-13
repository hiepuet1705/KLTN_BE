package com.example.DA.model;

import com.example.DA.model.enums.PostStatus;
import com.example.DA.model.enums.PostType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Posts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends DateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    private String postTitle;
    private String postContent;
    private Integer charged = 0;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Pattern(regexp = "pending|approved|rejected", message = "status must be 'pending', 'approved', or 'rejected'")
    @Column(name = "status", nullable = false)
    private String status = "pending";  // default value

    // Restrict postType to 'for_sale' or 'for_rent'
    @Pattern(regexp = "for_sale|for_rent", message = "postType must be 'for_sale' or 'for_rent'")
    @Column(name = "post_type", nullable = false)
    private String postType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long price;

}

