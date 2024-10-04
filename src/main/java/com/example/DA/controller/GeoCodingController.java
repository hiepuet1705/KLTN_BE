package com.example.DA.controller;

import com.example.DA.service.GeoCodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeoCodingController {

    @Autowired
    private GeoCodingService geoCodingService;

    @GetMapping("/get-coordinates")
    public String getCoordinates(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String phuong,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String province) {

        // Gọi GeoCodingService để lấy tọa độ từ địa chỉ đầy đủ
        Double[] coordinates = geoCodingService.getLatLonFromAddress(location, phuong, district, province);

        if (coordinates != null) {
            return "Latitude: " + coordinates[0] + ", Longitude: " + coordinates[1];
        } else {
            return "Không thể tìm thấy tọa độ cho địa chỉ này.";
        }
    }
}
