package com.example.DA.service.imp;

import com.example.DA.dto.UtilityDTO;
import com.example.DA.model.Property;
import com.example.DA.model.Utility;
import com.example.DA.repo.*;
import com.example.DA.service.DistanceService;
import com.example.DA.service.GeoCodingService;
import com.example.DA.service.UtilityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilityServiceImpl implements UtilityService {

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

    public List<UtilityDTO> getNearbyUtilities(Integer propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        double propertyLat = property.getLat();
        double propertyLon = property.getLon();

        List<Utility> allUtilities = utilityRepository.findAll();

        // Lọc danh sách utilities có khoảng cách nhỏ hơn 5km
        return allUtilities.stream()
                .filter(utility -> distanceService.calculateDistance(propertyLat, propertyLon, utility.getLat(), utility.getLon()) < 10)
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UtilityDTO getUtilityById(Integer utilityId) {
        Optional<Utility> optionalUtility = utilityRepository.findById(utilityId);
        return optionalUtility.map(this::convertToDTO).orElse(null);
    }

    @Override
    public UtilityDTO createUtility(UtilityDTO utilityDTO) {
        Double[] loc = geoCodingService.getLatLonFromAddress(utilityDTO.getLocation(), utilityDTO.getPhuong(), utilityDTO.getDistrict(), utilityDTO.getProvince());
        Utility utility = convertToEntity(utilityDTO);
        utility.setLat(loc[0]);
        utility.setLon(loc[1]);
        utility.setPhuong(phuongRepository.findByName(utilityDTO.getPhuong()).get(0));
        utility.setDistrict(districtRepository.findByName(utilityDTO.getDistrict()).get(0));
        utility.setProvince(provinceRepository.findByName(utilityDTO.getProvince()).get(0));
        utility = utilityRepository.save(utility);
        return convertToDTO(utility);
    }


    @Override
    public void deleteUtility(Integer utilityId) {
        utilityRepository.deleteById(utilityId);
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
