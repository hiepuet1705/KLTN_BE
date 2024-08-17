package com.example.DA.controller;


import com.example.DA.dto.AddFavoriteRequestDTO;
import com.example.DA.model.FavoriteList;
import com.example.DA.service.FavoriteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteListController {
    @Autowired
    private FavoriteListService favoriteListService;

    @PostMapping
    public ResponseEntity<FavoriteList> addFavorite(@RequestBody AddFavoriteRequestDTO requestDTO) {
        FavoriteList favoriteList = favoriteListService.addFavorite(requestDTO);
        return new ResponseEntity<>(favoriteList, HttpStatus.OK);
    }
}
