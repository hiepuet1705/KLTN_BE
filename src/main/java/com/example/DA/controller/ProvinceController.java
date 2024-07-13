package com.example.DA.controller;

import com.example.DA.dto.RegisterDto;
import com.example.DA.model.enums_entity.Province;
import com.example.DA.service.ProvinceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ProvinceController {
    private ProvinceService provinceService;

    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping("/provinces")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Province>> getAllProvinces() {
        List<Province> response = provinceService.getAllProvinces();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
