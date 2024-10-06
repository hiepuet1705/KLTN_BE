package com.example.DA.repo;

import com.example.DA.model.enums_entity.District;
import com.example.DA.model.enums_entity.Phuong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PhuongRepository extends JpaRepository<Phuong, Integer> {
    @Query("SELECT p FROM Phuong p WHERE p.name LIKE %:name%")
    List<Phuong> findByName(@Param("name") String name);
}
