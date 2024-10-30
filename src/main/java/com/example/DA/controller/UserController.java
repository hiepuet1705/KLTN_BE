package com.example.DA.controller;


import com.example.DA.dto.UserDTOResponse;
import com.example.DA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findByPhone")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTOResponse> getUserByPhone(@RequestParam String phone) {
        UserDTOResponse userDTOResponse = userService.findUserByPhone(phone);
        return new ResponseEntity<>(userDTOResponse, HttpStatus.OK);
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> test() {

        return new ResponseEntity<>("ooee", HttpStatus.OK);
    }

    @PostMapping("/send-verification-email")
    public void sendVerificationEmail(@RequestParam String email) {
        userService.sendVerificationEmail(email);
    }

    @PostMapping("/verify-user")
    public boolean verifyUser(@RequestParam String email, @RequestParam String code) {
        return userService.verifyUser(email, code);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserDTOResponse>> getAllUser() {
        List<UserDTOResponse> lists = userService.getAllUsers();
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTOResponse> getUserById(@PathVariable Integer id) {
        UserDTOResponse user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
