package com.example.DA.service;

import com.example.DA.dto.AddFavoriteRequestDTO;
import com.example.DA.model.FavoriteList;

public interface FavoriteListService {
    Integer addFavorite(AddFavoriteRequestDTO requestDTO);
}
