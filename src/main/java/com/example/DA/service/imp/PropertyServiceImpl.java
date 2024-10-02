package com.example.DA.service.imp;


import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PropertyDTORequest;
import com.example.DA.dto.PropertyDTOResponse;
import com.example.DA.exception.ApiException;
import com.example.DA.model.Property;
import com.example.DA.model.PropertyImage;
import com.example.DA.model.Utility;
import com.example.DA.repo.*;
import com.example.DA.service.DistanceService;
import com.example.DA.service.PropertyService;
import com.example.DA.service.S3Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyStatusRepository statusRepository;

    @Autowired
    private PropertyImageRepository propertyImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PhuongRepository phuongRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private S3Service s3Service;

    @Autowired
    DistanceService distanceService;

    @Override
    public PropertyDTOResponse saveProperty(PropertyDTORequest dto) {
        Property property = convertToEntityFromRequest(dto);
        property.setStatus(statusRepository.findById(dto.getStatusId()).orElse(null));
        property.setOwner(userRepository.findById(dto.getOwnerId()).orElse(null));
        property.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));

        property.setPhuong(phuongRepository.findByName(dto.getPhuong()).orElse(null));
        property.setDistrict(districtRepository.findByName(dto.getDistrict()));
        property.setProvince(provinceRepository.findByName(dto.getProvince()));
        Property savedProperty = propertyRepository.save(property);
        return convertToDTO(savedProperty);
    }

    @Override
    public PropertyDTOResponse getPropertyById(Integer propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new RuntimeException("property with " + propertyId + " not found"));
        return property != null ? convertToDTO(property) : null;
    }

    @Override
    public List<PropertyDTOResponse> getAllProperties() {
        return propertyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyDTOResponse> getPropertiesByUserId(Integer userId) {
        return propertyRepository.findPropertiesByOwnerId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Utility> getNearbyUtilities(Integer propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        double propertyLat = property.getLat();
        double propertyLon = property.getLon();

        List<Utility> allUtilities = utilityRepository.findAll();

        // Lọc danh sách utilities có khoảng cách nhỏ hơn 5km
        return allUtilities.stream()
                .filter(utility -> distanceService.calculateDistance(propertyLat, propertyLon, utility.getLat(), utility.getLon()) < 5)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteProperty(Integer propertyId) {
        propertyRepository.deleteById(propertyId);
    }

    @Override
    public List<String> uploadPropertyImages(Integer propertyId, MultipartFile[] files) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                // Upload file lên S3
                String imageUrl = s3Service.uploadFile(propertyId.toString(), file);
                // Tạo đối tượng PropertyImage để lưu thông tin vào CSDL
                PropertyImage propertyImage = new PropertyImage(property, imageUrl);
                propertyImageRepository.save(propertyImage);
                property.getImages().add(propertyImage);


                imageUrls.add(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        }
        // Lưu property sau khi đã thêm danh sách ảnh
        propertyRepository.save(property);
        return imageUrls;
    }


    public PropertyDTOResponse convertToDTO(Property property) {
        PropertyDTOResponse dto = modelMapper.map(property, PropertyDTOResponse.class);
        dto.setPhuong(property.getPhuong().getName());
        dto.setDistrict(property.getDistrict().getName());
        dto.setProvince(property.getProvince().getName());
        dto.setImages(property.getImages().stream().map(PropertyImage::getImageUrl).toList());
        return dto;
    }


    public Property convertToEntity(PropertyDTOResponse dto) {
        Property property = modelMapper.map(dto, Property.class);
        return property;
    }

    public Property convertToEntityFromRequest(PropertyDTORequest dto) {
        Property property = modelMapper.map(dto, Property.class);
        return property;
    }

}
