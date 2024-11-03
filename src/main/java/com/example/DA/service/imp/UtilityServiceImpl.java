package com.example.DA.service.imp;

import com.example.DA.dto.UtilityDTO;
import com.example.DA.model.Property;
import com.example.DA.model.Utility;
import com.example.DA.model.enums_entity.UtilityType;
import com.example.DA.repo.*;
import com.example.DA.service.DistanceService;
import com.example.DA.service.GeoCodingService;
import com.example.DA.service.UtilityService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UtilityServiceImpl implements UtilityService {

    private static final String OVERPASS_API_URL = "http://overpass-api.de/api/interpreter?data=[out:json];node(around:{radius},{lat},{lon})[amenity={type}];out;";


    @Autowired
    private UtilityRepository utilityRepository;

    @Autowired
    private UtilityTypeRepository utilityTypeRepository;

    @Autowired
    private PhuongRepository phuongRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private GeoCodingService geoCodingService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private DistanceService distanceService;

    @Override
    public List<UtilityDTO> getAllUtilities() {
        return utilityRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<UtilityDTO> getNearbySchools(Integer propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        double propertyLat = property.getLat();
        double propertyLon = property.getLon();
        int radius = 3000; // bán kính 3km

        // Fetch schools asynchronously
        CompletableFuture<List<UtilityDTO>> schoolsFuture = CompletableFuture.supplyAsync(() ->
                fetchAmenities(propertyLat, propertyLon, radius, "university")
        );

        // Fetch utilities asynchronously
        CompletableFuture<List<UtilityDTO>> utilitiesFuture = CompletableFuture.supplyAsync(() ->
                getNearbyUtilities(propertyId, "Trường học") // Replace "utility_type_name_here" with the actual type if needed
        );

        // Combine results once both futures are completed
        return schoolsFuture.thenCombine(utilitiesFuture, (schools, utilities) -> {
            schools.addAll(utilities);
            return schools;
        }).join(); // Blocks until both are complete and returns the combined list
    }

    public List<UtilityDTO> getNearbyHospitals(Integer propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        double propertyLat = property.getLat();
        double propertyLon = property.getLon();
        int radius = 3000; // bán kính 3km

        // Fetch hospitals asynchronously
        CompletableFuture<List<UtilityDTO>> hospitalsFuture = CompletableFuture.supplyAsync(() ->
                fetchAmenities(propertyLat, propertyLon, radius, "hospital")
        );

        // Fetch utilities asynchronously
        CompletableFuture<List<UtilityDTO>> utilitiesFuture = CompletableFuture.supplyAsync(() ->
                getNearbyUtilities(propertyId, "Bệnh viện") // Replace "utility_type_name_here" with the actual type if needed
        );


        // Combine results once all futures are completed
        return hospitalsFuture.thenCombine(utilitiesFuture, (hospitals, utilities) -> {
            hospitals.addAll(utilities);
            return hospitals;
        }).join(); // Blocks until all are complete and returns the combined list
    }


    private List<UtilityDTO> fetchAmenities(double lat, double lon, int radius, String amenityType) {
        try {
            // Tạo URL truy vấn Overpass API chỉ dựa trên loại tiện ích
            String url = OVERPASS_API_URL
                    .replace("{lat}", String.valueOf(lat))
                    .replace("{lon}", String.valueOf(lon))
                    .replace("{radius}", String.valueOf(radius))
                    .replace("{type}", amenityType);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            List<UtilityDTO> amenities = new ArrayList<>();

            if (root.has("elements")) {
                for (JsonNode element : root.get("elements")) {
                    UtilityDTO dto = new UtilityDTO();

                    JsonNode nameNode = element.get("tags").get("name");
                    if (nameNode != null)
                        dto.setUtilityName(nameNode.asText());
                    dto.setLat(element.get("lat").asDouble());
                    dto.setLon(element.get("lon").asDouble());

                    // Gán loại tiện ích
                    dto.setType(amenityType);

                    amenities.add(dto);
                }
            }

            return amenities;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public UtilityDTO getUtilityById(Integer utilityId) {
        Optional<Utility> optionalUtility = utilityRepository.findById(utilityId);
        return optionalUtility.map(this::convertToDTO).orElse(null);
    }

    @Override
    public UtilityDTO createUtility(UtilityDTO utilityDTO) {
        Utility utility = convertToEntity(utilityDTO);

        utility.setPhuong(phuongRepository.findByName(utilityDTO.getPhuong()).get(0));
        utility.setDistrict(districtRepository.findByName(utilityDTO.getDistrict()).get(0));
        utility.setProvince(provinceRepository.findByName(utilityDTO.getProvince()).get(0));

        // Save the utility entity before fetching lat/lon
        utility = utilityRepository.save(utility);

        // Call geoCodingService asynchronously
        CompletableFuture<Double[]> latLonFuture = CompletableFuture.supplyAsync(() ->
                geoCodingService.getLatLonFromAddress(utilityDTO.getLocation(), utilityDTO.getPhuong(), utilityDTO.getDistrict(), utilityDTO.getProvince())
        );

        // Wait for the result and set latitude and longitude
        try {
            Double[] loc = latLonFuture.join(); // Wait for the asynchronous task to complete
            utility.setLat(loc[0]);
            utility.setLon(loc[1]);

            // Optionally save the updated utility again, depending on your use case
            utility = utilityRepository.save(utility);
        } catch (Exception e) {
            // Handle exceptions appropriately
            utility.setLat(0.0);
            utility.setLon(0.0);
            utilityRepository.save(utility);
            throw new RuntimeException("Error fetching latitude and longitude", e);
        }

        return convertToDTO(utility);
    }


    @Override
    public void deleteUtility(Integer utilityId) {
        utilityRepository.deleteById(utilityId);
    }


    public List<UtilityDTO> getNearbyUtilities(Integer propertyId, String type) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        double propertyLat = property.getLat();
        double propertyLon = property.getLon();

        Optional<UtilityType> utilityType = utilityTypeRepository.findByUtilityTypeName(type);

        List<Utility> allUtilities = utilityRepository.findByType(utilityType.get().getUtilityTypeId());

        // Lọc danh sách utilities có khoảng cách nhỏ hơn 5km
        return allUtilities.stream()
                .filter(utility -> distanceService.calculateDistance(propertyLat, propertyLon, utility.getLat(), utility.getLon()) < 3)
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    private UtilityDTO convertToDTO(Utility utility) {

        UtilityDTO utilityDTO = modelMapper.map(utility, UtilityDTO.class);
        utilityDTO.setPhuong(utility.getPhuong().getName());
        utilityDTO.setDistrict(utility.getDistrict().getName());
        utilityDTO.setProvince(utility.getProvince().getName());
        return utilityDTO;

    }

    private Utility convertToEntity(UtilityDTO utilityDTO) {
        Utility utility = modelMapper.map(utilityDTO, Utility.class);
        utility.setUtilityType(utilityTypeRepository.findById(utilityDTO.getUtilityTypeId()).orElse(null));
        return utility;
    }
}
