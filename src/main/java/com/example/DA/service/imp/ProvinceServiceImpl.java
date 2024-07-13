package com.example.DA.service.imp;

import com.example.DA.model.enums_entity.Phuong;
import com.example.DA.model.enums_entity.Province;
import com.example.DA.repo.ProvinceRepository;
import com.example.DA.service.PhuongService;
import com.example.DA.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProvinceServiceImpl implements ProvinceService {
    ProvinceRepository provinceRepository;


    @Autowired
    public ProvinceServiceImpl(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }


    @Override
    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }
}
