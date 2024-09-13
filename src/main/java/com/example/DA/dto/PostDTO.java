package com.example.DA.dto;

import com.example.DA.model.enums.PostStatus;
import com.example.DA.model.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Integer postId;
    private String postTitle;
    private String postContent;
    private Integer charged;
    private Long price;
    private String status;
    private String postType;
    private Integer propertyId;
    private Integer userId;
}
