package com.example.DA.repo;

import com.example.DA.model.Utility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UtilityRepository extends JpaRepository<Utility, Integer> {
    @Query("SELECT u FROM Utility u WHERE u.utilityType.utilityTypeId = :type")
    List<Utility> findByType(@Param("type") Integer type);
}
