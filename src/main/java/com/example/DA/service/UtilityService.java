package com.example.DA.service;

import com.example.DA.dto.UtilityDTO;

import java.util.List;

public interface UtilityService {
    List<UtilityDTO> getAllUtilities();

    UtilityDTO getUtilityById(Integer utilityId);

    UtilityDTO createUtility(UtilityDTO utilityDTO);

    void deleteUtility(Integer utilityId);

    public List<UtilityDTO> getNearbyUtilities(Integer propertyId, String type);

    public List<UtilityDTO> getNearbySchools(Integer propertyId);


    public List<UtilityDTO> getNearbyHospitals(Integer propertyId);
}
