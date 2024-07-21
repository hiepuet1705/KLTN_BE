package com.example.DA.service;

import com.example.DA.dto.PropertyDTO;
import com.example.DA.model.Property;

import java.util.List;

public interface PropertyService {
    PropertyDTO saveProperty(PropertyDTO dto);

    PropertyDTO getPropertyById(Integer propertyId);

    List<PropertyDTO> getAllProperties();

    void deleteProperty(Integer propertyId);


}
