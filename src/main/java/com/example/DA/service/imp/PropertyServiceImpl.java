package com.example.DA.service.imp;


import com.example.DA.dto.PostSearchCriteria;
import com.example.DA.dto.PropertyDTORequest;
import com.example.DA.dto.PropertyDTOResponse;
import com.example.DA.dto.UtilityDTO;
import com.example.DA.exception.ApiException;
import com.example.DA.model.Property;
import com.example.DA.model.PropertyImage;
import com.example.DA.model.Utility;
import com.example.DA.repo.*;
import com.example.DA.service.DistanceService;
import com.example.DA.service.GeoCodingService;
import com.example.DA.service.PropertyService;
import com.example.DA.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    @Autowired
    private GeoCodingService geoCodingService;


    @Override
    @Transactional
    public PropertyDTOResponse patchProperty(Integer propertyId, PropertyDTORequest propertyDTORequest) {
        // Tìm Property theo ID
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with id: " + propertyId));

        // Cập nhật các trường chỉ định nếu có giá trị
        if (propertyDTORequest.getTitle() != null) {
            property.setTitle(propertyDTORequest.getTitle());
        }
        if (propertyDTORequest.getDescription() != null) {
            property.setDescription(propertyDTORequest.getDescription());
        }
        if (propertyDTORequest.getPrice() != null) {
            property.setPrice(propertyDTORequest.getPrice());
        }

        // Lưu cập nhật vào cơ sở dữ liệu
        propertyRepository.save(property);

        return convertToDTO(property);
    }


    public String updateCoordinates(Integer propertyId) {
        // Tìm kiếm Property dựa trên PropertyId
        Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new RuntimeException("property with " + propertyId + " not found"));


        String location = property.getLocation();
        String phuong = property.getPhuong() != null ? property.getPhuong().getName() : null;
        String district = property.getDistrict() != null ? property.getDistrict().getName() : null;
        String province = property.getProvince() != null ? property.getProvince().getName() : null;

        // Lấy tọa độ từ địa chỉ đầy đủ
        Double[] coordinates = geoCodingService.getLatLonFromAddress(location, phuong, district, province);
        if (coordinates != null) {
            property.setLat(coordinates[0]);
            property.setLon(coordinates[1]);
        }
        propertyRepository.save(property);
        return "Update Lat and Long with property  " + propertyId;

    }


    public PropertyDTOResponse updatePropertyStatus(Integer propertyId, String status) {
        // Tìm property theo ID
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found"));

        // Kiểm tra và cập nhật trạng thái
        if (!status.equals("approved")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        property.setStatus(status);
        propertyRepository.save(property);

        return convertToDTO(property);
    }

    @Override
    public PropertyDTOResponse saveProperty(PropertyDTORequest dto) {
        Property property = convertToEntityFromRequest(dto);
        property.setOwner(userRepository.findById(dto.getOwnerId()).orElse(null));
        property.setCategory(categoryRepository.findById(dto.getCategoryId()).orElse(null));
        Double[] coordinates = geoCodingService.getLatLonFromAddress(dto.getLocation(), dto.getPhuong(), dto.getDistrict(), dto.getProvince());
        if (coordinates != null) {
            property.setLat(coordinates[0]);
            property.setLon(coordinates[1]);
        }
        property.setPhuong(phuongRepository.findByName(dto.getPhuong()).get(0));
        property.setDistrict(districtRepository.findByName(dto.getDistrict()).get(0));
        property.setProvince(provinceRepository.findByName(dto.getProvince()).get(0));
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
    public List<PropertyDTOResponse> getPropertiesByStatus(String status) {
        if (!status.equals("pending") && !status.equals("approved") && !status.equals("rejected")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        // Tìm các bất động sản theo trạng thái
        List<Property> properties = propertyRepository.findByStatus(status);

        // Chuyển đổi danh sách Property thành danh sách PropertyDTOResponse
        return properties.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
                .filter(property -> "approved".equals(property.getStatus())) // Lọc các thuộc tính có status là "approved"
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public Integer getPropertiesByProvinceOrAll(String province, Integer month, Integer year) {
        List<Property> properties;

        if (province == null || province.isEmpty() || province.equals("all")) {
            properties = propertyRepository.findByStatus("approved");
        } else {
            Integer provinceId = provinceRepository.findByName(province).get(0).getId();
            properties = propertyRepository.findPropertiesByProvince(provinceId);
        }

        long numberOfProperties = properties.stream()
                .filter(property -> {
                    LocalDateTime createdAt = property.getCreatedAt(); // Giả sử bạn có phương thức này trong Property
                    return createdAt.getMonthValue() == month && createdAt.getYear() == year;
                })
                .count();

        return (int) numberOfProperties;

    }


    @Override
    public void deleteProperty(Integer propertyId) {
        propertyRepository.deleteById(propertyId);
    }


    @Transactional
    public PropertyDTOResponse savePropertyWithImages(PropertyDTORequest dto, MultipartFile[] files) {
        // Convert DTO to Property entity and populate fields
        Property property = convertToEntityFromRequest(dto);

        property.setOwner(userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found")));

        property.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found")));

        property.setPhuong(phuongRepository.findByName(dto.getPhuong())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Phường not found")));

        property.setDistrict(districtRepository.findByName(dto.getDistrict())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("District not found")));

        property.setProvince(provinceRepository.findByName(dto.getProvince())
                .stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Province not found")));

        // Step 1: Run geolocation task asynchronously
        CompletableFuture<Double[]> coordinatesFuture = CompletableFuture.supplyAsync(() ->
                geoCodingService.getLatLonFromAddress(dto.getLocation(), dto.getPhuong(), dto.getDistrict(), dto.getProvince())
        );

        // Step 2: Save the property synchronously
        Property savedProperty = propertyRepository.save(property);

        // Step 3: Wait for the geolocation result
        Double[] coordinates = coordinatesFuture.join(); // Wait for the geolocation to finish

        // Step 4: Update property with coordinates if available
        if (coordinates != null) {
            savedProperty.setLat(coordinates[0]);
            savedProperty.setLon(coordinates[1]);
        }

        // Step 5: Upload images
        List<String> imageUrls = uploadPropertyImages(savedProperty.getPropertyId(), files);

        // Step 6: Save the property again to update coordinates if they were set
        propertyRepository.save(savedProperty);

        // Step 7: Return the saved DTO
        return convertToDTO(savedProperty);
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


    public Property convertToEntityFromRequest(PropertyDTORequest dto) {
        Property property = new Property();
        property = modelMapper.map(dto, Property.class);
        return property;
    }

}
