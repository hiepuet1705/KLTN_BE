package com.example.DA.repo;


import com.example.DA.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Integer> {
    Optional<OTP> findByEmailAndCode(String email, String code);

    void deleteByEmail(String email);
}
