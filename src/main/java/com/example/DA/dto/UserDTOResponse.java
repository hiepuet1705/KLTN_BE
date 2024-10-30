package com.example.DA.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOResponse {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String username;
    private Integer isVerified;
}
