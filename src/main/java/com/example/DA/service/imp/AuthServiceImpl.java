package com.example.DA.service.imp;


import com.example.DA.dto.LoginDto;
import com.example.DA.dto.RegisterDto;
import com.example.DA.exception.ApiException;
import com.example.DA.model.Role;
import com.example.DA.model.User;
import com.example.DA.repo.RoleRepository;
import com.example.DA.repo.UserRepository;
import com.example.DA.security.JwtTokenProvider;
import com.example.DA.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return token;


    }

    @Override
    public String register(RegisterDto registerDto) {
        // add check for username exists in database

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Username is already exists");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already exists");
        }

        // map
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(com.example.DA.enums.Role.RoleUser).get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        return "User register successfully";

    }

    @Override
    public String registerAdmin(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Username is already exists");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already exists");
        }

        // map
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(com.example.DA.enums.Role.RoleAdmin).get();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
        return "Admin register successfully";
    }
}
