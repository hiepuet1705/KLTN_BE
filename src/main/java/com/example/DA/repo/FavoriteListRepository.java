package com.example.DA.repo;

import com.example.DA.model.FavoriteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FavoriteListRepository extends JpaRepository<FavoriteList, Integer> {

}
