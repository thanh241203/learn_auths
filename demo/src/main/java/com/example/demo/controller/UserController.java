package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    /**
     * Trích xuất tên người dùng
     *
     * @return user detail
     */
    @GetMapping("/info")
    public Optional<User> getUserDetails() {
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        return optionalUser;
    }
}
