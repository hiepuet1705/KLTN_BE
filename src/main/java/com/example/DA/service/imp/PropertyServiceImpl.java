package com.example.DA.service.imp;

import com.example.DA.dto.PropertyDTO;
import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.exception.ApiException;
import com.example.DA.model.Property;
import com.example.DA.model.Utility;
import com.example.DA.repo.*;
import com.example.DA.service.DistanceService;
import com.example.DA.service.PropertyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyStatusRepository statusRepository;

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
    DistanceService distanceService;

    @Override
    public PropertyDTO saveProperty(PropertyDTO dto) {
        Property property = convertToEntity(dto);

        property.setStatus(statusRepository.findById(dto.getStatusId()).orElse(null));
        property.setOwner(userRepository.findById(dto.getOwnerId()).orElse(null));
        property.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));

        property.setPhuong(phuongRepository.findByName(dto.getPhuong()).orElse(null));
        property.setDistrict(districtRepository.findByName(dto.getDistrict()).orElse(null));
        property.setProvince(provinceRepository.findByName(dto.getProvince()));
        Property savedProperty = propertyRepository.save(property);
        return convertToDTO(savedProperty);
    }

    @Override
    public PropertyDTO getPropertyById(Integer propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new RuntimeException("property with " + propertyId + " not found"));
        return property != null ? convertToDTO(property) : null;
    }

    @Override
    public List<PropertyDTO> getAllProperties() {
        return propertyRepository.findAll().stream()
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
    public PropertyDTO updatePropertyById(Integer propertyId, PropertyDTO propertyDTO) {
        // Lấy property từ database hoặc ném ngoại lệ nếu không tìm thấy
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Property not found"));
        modelMapper.map(propertyDTO, property);
        Property updatedProperty = propertyRepository.save(property);
        return convertToDTO(updatedProperty);
    }


    @Override
    public void deleteProperty(Integer propertyId) {
        propertyRepository.deleteById(propertyId);
    }


    public PropertyDTO convertToDTO(Property property) {
        PropertyDTO dto = modelMapper.map(property, PropertyDTO.class);
        dto.setPhuong(property.getPhuong().getName());
        dto.setDistrict(property.getDistrict().getName());
        dto.setProvince(property.getProvince().getName());
        return dto;
    }

    public Property convertToEntity(PropertyDTO dto) {
        Property property = modelMapper.map(dto, Property.class);

        return property;
    }

}
