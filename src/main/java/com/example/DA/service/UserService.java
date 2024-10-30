package com.example.DA.service;

import com.example.DA.dto.UserDTOResponse;
import com.example.DA.model.User;

import java.util.List;

public interface UserService {
    UserDTOResponse findUserByPhone(String phone);

    List<UserDTOResponse> getAllUsers();

    public void sendVerificationEmail(String email);


    public boolean verifyUser(String email, String code);
}
