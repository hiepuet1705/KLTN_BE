package com.example.DA.repo;

import com.example.DA.model.enums_entity.District;
import com.example.DA.model.enums_entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    @Query("SELECT p FROM Province p WHERE p.name LIKE %:name%")
    List<Province> findByName(@Param("name") String name);

}
