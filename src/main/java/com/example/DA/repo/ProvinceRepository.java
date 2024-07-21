package com.example.DA.repo;

import com.example.DA.model.enums_entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    Optional<Province> findByName(String name);
}
