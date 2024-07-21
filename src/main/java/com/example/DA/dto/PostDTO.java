package com.example.DA.dto;

import com.example.DA.model.enums.PostStatus;
import com.example.DA.model.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Integer postId;
    private String postTitle;
    private String postContent;
    private Integer propertyId;
    private PostStatus status;
    private PostType postType;
}
