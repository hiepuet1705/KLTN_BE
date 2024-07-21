package com.example.DA.repo;

import com.example.DA.model.enums_entity.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PropertyStatusRepository extends JpaRepository<PropertyStatus, Integer> {
}
