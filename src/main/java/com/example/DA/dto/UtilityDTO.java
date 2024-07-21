package com.example.DA.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UtilityDTO {
    private Integer utilityId;
    private String utilityName;
    private String location;
    private Integer utilityTypeId;
    private Integer phuongId;
    private Integer districtId;
    private Integer provinceId;
}
