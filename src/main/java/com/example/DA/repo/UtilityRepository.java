package com.example.DA.repo;

import com.example.DA.model.Utility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UtilityRepository extends JpaRepository<Utility, Integer> {
}
