package com.example.DA.controller;


import com.example.DA.dto.PropertyDTORequest;
import com.example.DA.dto.PropertyDTOResponse;
import com.example.DA.dto.UtilityDTO;
import com.example.DA.service.PropertyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PutMapping("/{propertyId}/status/approved")
    public ResponseEntity<PropertyDTOResponse> updateStatusToApproved(@PathVariable Integer propertyId) {
        try {
            // Cập nhật trạng thái của property thành "approved"
            PropertyDTOResponse updatedProperty = propertyService.updatePropertyStatus(propertyId, "approved");
            return new ResponseEntity<>(updatedProperty, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


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

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PropertyDTOResponse> getPropertyById(@PathVariable Integer id) {
        PropertyDTOResponse properties = propertyService.getPropertyById(id);
        return ResponseEntity.ok(properties);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PropertyDTOResponse>> getAllProperty() {
        List<PropertyDTOResponse> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }


}
