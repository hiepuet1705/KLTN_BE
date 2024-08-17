package com.example.DA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTO {
    private Integer propertyId;
    private Integer statusId;
    private Integer ownerId;
    private String title;
    private String description;
    private Integer price;
    private Integer categoryId;
    private String location;
    private String phuong;
    private String district;
    private String province;
    private Double area;

}

