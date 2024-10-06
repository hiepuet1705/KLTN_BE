package com.example.DA.controller;


import com.example.DA.model.enums_entity.Phuong;

import com.example.DA.service.PhuongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PhuongController {

    @Autowired
    private PhuongService phuongService;

    @GetMapping("/phuongs")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Phuong>> getAllProvinces() {
        List<Phuong> response = phuongService.getAllPhuong();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}
