package com.example.DA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchCriteria {
    private String district;
    private String province;
    private Integer minPrice;
    private Integer maxPrice;
    private String postType;
    private Double minArea;
    private Double maxArea;
    private String propertyType;
}
