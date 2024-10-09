package com.example.DA.repo;

import com.example.DA.model.FavoriteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FavoriteListRepository extends JpaRepository<FavoriteList, Integer> {

    @Query("SELECT fl FROM FavoriteList fl WHERE fl.user.id = :userId")
    List<FavoriteList> findByUser_UserId(@Param("userId") Integer userId);

}
