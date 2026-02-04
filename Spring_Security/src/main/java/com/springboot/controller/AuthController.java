package com.springboot.controller;

import com.springboot.dto.AuthRequest;
import com.springboot.dto.AuthResponse;
import com.springboot.dto.RegisterRequest;
import com.springboot.entity.User;
import com.springboot.repository.UserRepository;
import com.springboot.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    //  REGISTER
    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return "User registered successfully";
    }

    //  LOGIN
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getUsername());
        return new AuthResponse(token);
    }
}
