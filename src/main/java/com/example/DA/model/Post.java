package com.example.DA.model;

import com.example.DA.model.enums.PostStatus;
import com.example.DA.model.enums.PostType;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('pending', 'approved', 'rejected')")
    private PostStatus status = PostStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('for_sale', 'for_rent')")
    private PostType postType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long price;

}

