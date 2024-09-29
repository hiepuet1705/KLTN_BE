package com.example.DA.repo;

import com.example.DA.model.enums_entity.District;
import com.example.DA.model.enums_entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    @Query("SELECT d FROM District d WHERE d.name = :name")
    District findByName(@Param("name") String name);
}
