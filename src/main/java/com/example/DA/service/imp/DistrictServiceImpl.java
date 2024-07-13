package com.example.DA.service.imp;


import com.example.DA.model.enums_entity.District;
import com.example.DA.repo.DistrictRepository;
import com.example.DA.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    DistrictRepository districtRepository;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Override
    public List<District> getAllDistrict() {
        return districtRepository.findAll();
    }
}
