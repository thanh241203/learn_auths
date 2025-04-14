package com.example.demo.security;

import com.example.demo.filter.JWTFilter;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 * Vô hiệu hóa CSRF & auth basic
 * cho phép free-access authenication to url :"/api/auth/**"
 * gán ROLE_USER cho url :"/api/user/**"
 * thêm jwt filter cho request validation
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JWTFilter jwtFilter;

    private final MyUserDetailService getUserDetailService;

    public SecurityConfig( JWTFilter jwtFilter, MyUserDetailService getUserDetailService) {
        this.jwtFilter = jwtFilter;
        this.getUserDetailService = getUserDetailService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity htpp,HandlerMappingIntrospector  introspector) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);

        htpp.csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/user/**")).permitAll()
                        .anyRequest().authenticated())
                .userDetailsService(getUserDetailService)
                .exceptionHandling(excetp -> excetp.authenticationEntryPoint((request, response, authException)
                        -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        htpp.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return htpp.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(getUserDetailService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

}
