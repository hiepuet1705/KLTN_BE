package com.example.DA.service;


import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PropertyDTORequest;
import com.example.DA.dto.PropertyDTOResponse;
import com.example.DA.dto.UtilityDTO;
import com.example.DA.model.Post;
import com.example.DA.model.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {
    PropertyDTOResponse saveProperty(PropertyDTORequest dto);


    PropertyDTOResponse getPropertyById(Integer propertyId);

    List<PropertyDTOResponse> getAllProperties();

    List<PropertyDTOResponse> getPropertiesByUserId(Integer userId);

//    PropertyDTOResponse updatePropertyById(Integer propertyId, PropertyDTO propertyDTO);

    void deleteProperty(Integer propertyId);

    public List<String> uploadPropertyImages(Integer propertyId, MultipartFile[] files);

    public PropertyDTOResponse savePropertyWithImages(PropertyDTORequest dto, MultipartFile[] files);


}
