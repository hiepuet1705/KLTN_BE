package com.example.DA.controller;


import com.example.DA.dto.UtilityDTO;
import com.example.DA.model.Utility;
import com.example.DA.service.StatisticService;
import com.example.DA.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping("/average-price")
    public ResponseEntity<Map<String, Object>> getAveragePrice(
            @RequestParam(required = false, defaultValue = "all") String province,
            @RequestParam Integer month,
            @RequestParam Integer year, @RequestParam String purpose) {
        String averagePrice = statisticService.getAveragePriceByProvince(province, month, year, purpose);

        Map<String, Object> response = new HashMap<>();
        response.put("province", province);
        response.put("month", month);
        response.put("year", year);
        response.put("averagePrice", averagePrice);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getTotalRevenue(@RequestParam Integer month, @RequestParam Integer year) {
        Double totalRevenue = statisticService.getTotalRevenue(month, year);

        Map<String, Object> response = new HashMap<>();
        response.put("month", month);
        response.put("year", year);
        response.put("totalRevenue", totalRevenue);

        return ResponseEntity.ok(response);
    }

}
