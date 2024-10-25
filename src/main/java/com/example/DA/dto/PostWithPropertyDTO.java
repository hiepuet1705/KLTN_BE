package com.example.DA.dto;


import com.example.DA.model.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PostWithPropertyDTO {
    private Integer postId;
    private String postTitle;
    private String postContent;
    private Integer charged;
    private Long price;
    private String status;
    private String postType;
    private Integer paymentStatus;
    private PropertyDTOResponse property;
    private Integer userId;
}
