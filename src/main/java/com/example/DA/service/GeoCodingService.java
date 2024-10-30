package com.example.DA.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GeoCodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?q={address}&format=json&limit=1";

    public Double[] getLatLonFromAddress(String location, String phuong, String district, String province) {
        try {
            // Kết hợp các phần của địa chỉ thành một chuỗi duy nhất
            String fullAddress = buildFullAddress(location, phuong, district, province);

            // Mã hóa địa chỉ
            String encodedAddress = URLEncoder.encode(fullAddress, StandardCharsets.UTF_8.toString());
            String url = NOMINATIM_URL.replace("{address}", encodedAddress);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(NOMINATIM_URL, String.class, fullAddress);

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            if (root.isArray() && root.size() > 0) {
                JsonNode locationNode = root.get(0);
                Double lat = locationNode.get("lat").asDouble();
                Double lon = locationNode.get("lon").asDouble();
                return new Double[]{lat, lon};
            } else {
                return null; // Nếu không tìm thấy tọa độ
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildFullAddress(String location, String phuong, String district, String province) {
        StringBuilder fullAddress = new StringBuilder();

        // Kết hợp từng phần của địa chỉ nếu không null
        if (location != null && !location.isEmpty()) {
            fullAddress.append(location).append(", ");
        }
        if (phuong != null && !phuong.isEmpty()) {
            fullAddress.append(phuong).append(", ");
        }
        if (district != null && !district.isEmpty()) {
            fullAddress.append(district).append(", ");
        }
        if (province != null && !province.isEmpty()) {
            fullAddress.append(province);
        }

        // Loại bỏ các dấu phẩy thừa ở cuối chuỗi
        String result = fullAddress.toString().trim();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
