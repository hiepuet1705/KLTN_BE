package com.example.DA.service;


import com.example.DA.model.enums_entity.UtilityType;
import com.example.DA.repo.UtilityTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilityTypeService {
    @Autowired
    private UtilityTypeRepository utilityTypeRepository;

    public List<UtilityType> getAllUtilityType() {
        return utilityTypeRepository.findAll();
    }
}
