package com.example.DA.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddFavoriteRequestDTO {
    @NotNull
    private Integer userId;

    @NotNull
    private Integer postId;
}
