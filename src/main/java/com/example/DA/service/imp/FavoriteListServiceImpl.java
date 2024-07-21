package com.example.DA.service.imp;

import com.example.DA.dto.AddFavoriteRequestDTO;
import com.example.DA.model.FavoriteList;
import com.example.DA.model.Property;
import com.example.DA.model.User;
import com.example.DA.repo.FavoriteListRepository;
import com.example.DA.repo.PropertyRepository;
import com.example.DA.repo.UserRepository;
import com.example.DA.service.FavoriteListService;
import org.springframework.beans.factory.annotation.Autowired;

public class FavoriteListServiceImpl implements FavoriteListService {

    @Autowired
    private FavoriteListRepository favoriteListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;


    @Override
    public FavoriteList addFavorite(AddFavoriteRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Property property = propertyRepository.findById(requestDTO.getPropertyId()).orElseThrow(() -> new RuntimeException("Property not found"));

        FavoriteList favoriteList = new FavoriteList();
        favoriteList.setUser(user);
        favoriteList.setProperty(property);

        return favoriteListRepository.save(favoriteList);
    }
}
