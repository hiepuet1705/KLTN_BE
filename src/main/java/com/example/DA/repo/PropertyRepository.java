package com.example.DA.repo;


import com.example.DA.model.Property;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
    @Query("SELECT p FROM Property p WHERE p.owner.id = :ownerId")
    List<Property> findPropertiesByOwnerId(@Param("ownerId") Integer ownerId);


    @Query("SELECT p FROM Property p WHERE p.status = :status")
    List<Property> findByStatus(@Param("status") String status);

}
