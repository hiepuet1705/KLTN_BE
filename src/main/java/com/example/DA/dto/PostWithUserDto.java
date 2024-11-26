package com.example.DA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostWithUserDto {
    private Integer postId;
    private String postTitle;
    private String postContent;
    private Integer charged;
    private Long price;
    private String status;
    private String postType;
    private Integer paymentStatus = 0;
    private PropertyDTOResponse property;
    
}
