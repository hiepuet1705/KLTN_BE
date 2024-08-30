package com.example.DA.service;

import com.example.DA.dto.PropertyDTO;
import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertyService {
    PropertyDTO saveProperty(PropertyDTO dto);

    PropertyDTO getPropertyById(Integer propertyId);

    List<PropertyDTO> getAllProperties();

    PropertyDTO updatePropertyById(Integer propertyId, PropertyDTO propertyDTO);

    void deleteProperty(Integer propertyId);


}
