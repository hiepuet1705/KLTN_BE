package com.example.DA.repo;

import com.example.DA.model.enums_entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    Optional<District> findByName(String name);
}
