package com.example.DA.controller;


import com.example.DA.dto.PropertyDTO;
import com.example.DA.model.Property;
import com.example.DA.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PropertyDTO> createProperty(@RequestBody PropertyDTO propertyDTO) {
        PropertyDTO savedProperty = propertyService.saveProperty(propertyDTO);
        return ResponseEntity.ok(savedProperty);
    }


    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PropertyDTO>> getAllProperty() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }


}
