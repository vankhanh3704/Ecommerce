package com.hikiw.ecommerce.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    // đầu tiên muốn xây dựng hệ thống bảo mật , login jwt

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        // đoạn này là mình authorize những api nào là có quyền hay không có quyền được vào
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );

        // đoạn này là phải authenticated ( login) ms request đc vào system
//        httpSecurity
//                .oauth2ResourceServer(
//                        oauth2 -> oauth2
//                                .jwt(jwtConfigurer -> jwtConfigurer
//                                        .decoder()
//                                        )
//                                //xử lý ngoại lệ khi người dùng chưa xác thực (unauthenticated) mà cố truy cập tài nguyên cần bảo vệ.
//                                .authenticationEntryPoint(
//                                        new JwtAuthenticationEntryPoint() // file cấu hình xong
//
//                                        )
//                );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
