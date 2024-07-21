package com.example.DA.service;

import com.example.DA.dto.UtilityDTO;

import java.util.List;

public interface UtilityService {
    List<UtilityDTO> getAllUtilities();

    UtilityDTO getUtilityById(Integer utilityId);

    UtilityDTO createUtility(UtilityDTO utilityDTO);

    UtilityDTO updateUtility(Integer utilityId, UtilityDTO utilityDTO);

    void deleteUtility(Integer utilityId);
}
