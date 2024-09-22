package com.example.DA.dto;


import com.example.DA.model.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(indexName = "posts")
public class PostWithPropertyDTO {
    private Integer postId;
    private String postTitle;
    private String postContent;
    private Integer charged;
    private Long price;
    private String status;
    private String postType;
    private PropertyDTO property;
    private Integer userId;
}
