package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * lấy thông tin chi tiết của người dùng từ database
 * gán roles để authorization
 */
@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userRes = userRepository.findByUserName(username);

        if (userRes.isEmpty()) {
            throw new UsernameNotFoundException("khong tim thay user infor");
        }
        User user = userRes.get();
        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassWord(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
