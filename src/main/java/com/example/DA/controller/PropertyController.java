package com.example.DA.controller;


import com.example.DA.dto.PropertyDTORequest;
import com.example.DA.dto.PropertyDTOResponse;
import com.example.DA.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    @Autowired
    private PropertyService propertyService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PropertyDTOResponse> createProperty(@RequestBody PropertyDTORequest propertyDTO) {

        PropertyDTOResponse savedProperty = propertyService.saveProperty(propertyDTO);
        return ResponseEntity.ok(savedProperty);
    }

    @PostMapping("/properties-with-images")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPropertyWithImages(
            @RequestPart("property") PropertyDTORequest propertyDTO,
            @RequestPart("images") MultipartFile[] files) {

        try {
            PropertyDTOResponse savedProperty = propertyService.savePropertyWithImages(propertyDTO, files);
            return ResponseEntity.ok(savedProperty);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


//    @PostMapping
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<PropertyDTOResponse> createPropertyWithImage(@RequestBody PropertyDTORequest propertyDTO) {
//
//        PropertyDTOResponse savedProperty = propertyService.saveProperty(propertyDTO);
//        return ResponseEntity.ok(savedProperty);
//    }

    @PostMapping("/{propertyId}/images")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> uploadImage(@PathVariable Integer propertyId,
                                              @RequestParam("images") MultipartFile[] files) {

        try {
            propertyService.uploadPropertyImages(propertyId, files);
            return ResponseEntity.ok("Upload thanh cong");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading images: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public List<PropertyDTOResponse> getPropertiesByUserId(@PathVariable Integer userId) {
        return propertyService.getPropertiesByUserId(userId);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<PropertyDTORequest> updateProperty(@PathVariable("id") Integer propertyId, @RequestBody PropertyDTO propertyDTO) {
//        PropertyDTORequest updatedProperty = propertyService.updatePropertyById(propertyId, propertyDTO);
//        return ResponseEntity.ok(updatedProperty);
//    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PropertyDTOResponse>> getAllProperty() {
        List<PropertyDTOResponse> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }


}
