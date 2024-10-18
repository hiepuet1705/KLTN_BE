package com.example.DA.controller;


import com.example.DA.model.enums_entity.UtilityType;
import com.example.DA.service.UtilityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/utilityTypes")
public class UtilityTypeController {
    @Autowired
    private UtilityTypeService utilityTypeService;

    @GetMapping
    public List<UtilityType> getAllUtilities() {
        return utilityTypeService.getAllUtilityType();
    }
}
