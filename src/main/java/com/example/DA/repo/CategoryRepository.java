package com.example.DA.repo;

import com.example.DA.model.enums_entity.Category;
import com.example.DA.model.enums_entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.categoryName = :name")
    Category findByName(@Param("name") String name);
}
