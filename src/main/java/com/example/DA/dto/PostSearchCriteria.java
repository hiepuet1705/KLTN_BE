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
    private String status;
    private Double minArea = 0.0;
    private Double maxArea = 10000000000.0;
}
