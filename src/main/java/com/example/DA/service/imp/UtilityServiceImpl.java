package com.example.DA.service.imp;

import com.example.DA.dto.UtilityDTO;
import com.example.DA.model.Utility;
import com.example.DA.repo.*;
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
    private ModelMapper modelMapper;

    @Override
    public List<UtilityDTO> getAllUtilities() {
        return utilityRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UtilityDTO getUtilityById(Integer utilityId) {
        Optional<Utility> optionalUtility = utilityRepository.findById(utilityId);
        return optionalUtility.map(this::convertToDTO).orElse(null);
    }

    @Override
    public UtilityDTO createUtility(UtilityDTO utilityDTO) {
        Utility utility = convertToEntity(utilityDTO);
        utility = utilityRepository.save(utility);
        return convertToDTO(utility);
    }

    @Override
    public UtilityDTO updateUtility(Integer utilityId, UtilityDTO utilityDTO) {
        Optional<Utility> optionalUtility = utilityRepository.findById(utilityId);
        if (optionalUtility.isPresent()) {
            Utility utility = optionalUtility.get();
            modelMapper.map(utilityDTO, utility);
            utility.setUtilityType(utilityTypeRepository.findById(utilityDTO.getUtilityTypeId()).orElse(null));
            utility.setPhuong(phuongRepository.findById(utilityDTO.getPhuongId()).orElse(null));
            utility.setDistrict(districtRepository.findById(utilityDTO.getDistrictId()).orElse(null));
            utility.setProvince(provinceRepository.findById(utilityDTO.getProvinceId()).orElse(null));
            utility = utilityRepository.save(utility);
            return convertToDTO(utility);
        }
        return null;
    }

    @Override
    public void deleteUtility(Integer utilityId) {
        utilityRepository.deleteById(utilityId);
    }

    private UtilityDTO convertToDTO(Utility utility) {
        return modelMapper.map(utility, UtilityDTO.class);
    }

    private Utility convertToEntity(UtilityDTO utilityDTO) {
        Utility utility = modelMapper.map(utilityDTO, Utility.class);
        utility.setUtilityType(utilityTypeRepository.findById(utilityDTO.getUtilityTypeId()).orElse(null));
        utility.setPhuong(phuongRepository.findById(utilityDTO.getPhuongId()).orElse(null));
        utility.setDistrict(districtRepository.findById(utilityDTO.getDistrictId()).orElse(null));
        utility.setProvince(provinceRepository.findById(utilityDTO.getProvinceId()).orElse(null));
        return utility;
    }
}
