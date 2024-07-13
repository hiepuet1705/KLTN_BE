package com.example.DA.service;


import com.example.DA.dto.LoginDto;
import com.example.DA.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    String registerAdmin(RegisterDto registerDto);
}
