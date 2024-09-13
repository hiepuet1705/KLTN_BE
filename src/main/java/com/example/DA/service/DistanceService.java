package com.example.DA.service;

import org.springframework.stereotype.Service;

@Service
public class DistanceService {

    private static final double EARTH_RADIUS = 6371; // Earth's radius in kilometers

    // Hàm tính khoảng cách giữa 2 tọa độ (latitude và longitude)
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Trả về khoảng cách theo km
    }
}
