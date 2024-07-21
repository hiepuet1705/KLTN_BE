package com.example.DA.repo;

import com.example.DA.model.enums_entity.Phuong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PhuongRepository extends JpaRepository<Phuong, Integer> {
    Optional<Phuong> findByName(String name);
}
