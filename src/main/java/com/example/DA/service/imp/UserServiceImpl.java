package com.example.DA.service.imp;


import com.example.DA.dto.UserDTOResponse;
import com.example.DA.model.User;
import com.example.DA.repo.UserRepository;
import com.example.DA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDTOResponse findUserByPhone(String phone) {
        User user = userRepository.findUserByPhone(phone);
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        userDTOResponse.setPhone(user.getPhone());
        userDTOResponse.setId(user.getId());
        userDTOResponse.setName(user.getName());
        userDTOResponse.setUsername(user.getUsername());
        userDTOResponse.setEmail(user.getEmail());
        return userDTOResponse;
    }

    public List<UserDTOResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDTOResponse userDTOResponse = new UserDTOResponse();
                    userDTOResponse.setPhone(user.getPhone());
                    userDTOResponse.setId(user.getId());
                    userDTOResponse.setName(user.getName());
                    userDTOResponse.setUsername(user.getUsername());
                    userDTOResponse.setEmail(user.getEmail());
                    return userDTOResponse;
                }).collect(Collectors.toList());
    }
}
