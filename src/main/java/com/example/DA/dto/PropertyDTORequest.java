package com.example.DA.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTORequest {
    private Integer propertyId;
    private Integer statusId;
    private Integer ownerId;
    private String title;
    private String description;
    private Long price;
    private Integer categoryId;
    private String location;
    private String phuong;
    private String district;
    private String province;
    private String status = "pending";
    private Double area;
    private Integer sophong;
    private Integer soTang;
    private Integer soToilet;
    private Double lat;
    private Double lon;
    private Integer age;
}

