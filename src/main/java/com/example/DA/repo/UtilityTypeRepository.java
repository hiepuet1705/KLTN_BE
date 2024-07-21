package com.example.DA.repo;


import com.example.DA.model.enums_entity.UtilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilityTypeRepository extends JpaRepository<UtilityType, Integer> {
}
