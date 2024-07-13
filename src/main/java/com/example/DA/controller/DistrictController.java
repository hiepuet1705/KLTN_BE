package com.example.DA.controller;

import com.example.DA.model.enums_entity.District;
import com.example.DA.model.enums_entity.Province;
import com.example.DA.service.DistrictService;
import com.example.DA.service.ProvinceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DistrictController {
    private DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping("/districts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<District>> getAllProvinces() {
        List<District> response = districtService.getAllDistrict();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
