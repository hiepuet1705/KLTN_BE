package com.example.DA.repo;


import com.example.DA.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Integer> {
    Optional<OTP> findByEmailAndCode(String email, String code);

    @Modifying
    @Transactional
    @Query("DELETE FROM OTP o WHERE o.email = :email")
    void deleteByEmail(@Param("email") String email);
}
