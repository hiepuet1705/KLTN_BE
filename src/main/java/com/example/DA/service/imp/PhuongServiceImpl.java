package com.example.DA.service.imp;


import com.example.DA.model.enums_entity.Phuong;
import com.example.DA.repo.PhuongRepository;
import com.example.DA.service.PhuongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhuongServiceImpl implements PhuongService {
    @Autowired
    PhuongRepository phuongRepository;

    public PhuongServiceImpl(PhuongRepository phuongRepository) {
        this.phuongRepository = phuongRepository;
    }

    @Override
    public List<Phuong> getAllPhuong() {
        return phuongRepository.findAll();
    }

    @Override
    public Phuong findByNameContaining(String name) {
        return phuongRepository.findByName(name).get(0);
    }
}
