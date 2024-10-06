package com.example.DA.repo;


import com.example.DA.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);


    @Query("SELECT u FROM User u WHERE u.phone = :phone")
    User findUserByPhone(@Param("phone") String phone);

    Boolean existsByEmail(String email);
}
