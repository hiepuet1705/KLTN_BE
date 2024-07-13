package com.example.DA.service.imp;


import com.example.DA.repo.PhuongRepository;
import org.springframework.stereotype.Service;

@Service
public class PhuongServiceImpl {
    PhuongRepository phuongRepository;

    public PhuongServiceImpl(PhuongRepository phuongRepository) {
        this.phuongRepository = phuongRepository;
    }
}
