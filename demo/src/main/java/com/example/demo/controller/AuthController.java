package com.example.demo.controller;


import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/registers")
    public Map<String,Object> registerHandler(
            @RequestBody User user
            ){
        System.out.println("in here");
        String encodePass = passwordEncoder.encode(user.getPassWord());
        user.setPassWord(encodePass);
        user=userRepo.save(user);
        String token = jwtUtil.generateToken(user.getUserName());
        return Collections.singletonMap("jwt-token",token);
    }


}
