package com.example.DA.service;

import com.example.DA.model.enums_entity.Phuong;

import java.util.List;

public interface PhuongService {
    List<Phuong> getAllPhuong();

    Phuong findByNameContaining(String name);
}
