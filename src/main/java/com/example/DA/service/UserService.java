package com.example.DA.service;

import com.example.DA.dto.UserDTOResponse;
import com.example.DA.model.User;

public interface UserService {
    UserDTOResponse findUserByPhone(String phone);
}
