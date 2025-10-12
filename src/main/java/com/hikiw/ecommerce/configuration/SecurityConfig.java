package com.hikiw.ecommerce.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // các api public( không cầu authen)
    private final String[] PUBLIC_ENDPOINTS = {  "/users", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh" };


    CustomerJwtDecoder customerJwtDecoder;
    // đầu tiên muốn xây dựng hệ thống bảo mật , login jwt

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        // đoạn này là mình authorize những api nào là có quyền hay không có quyền được vào
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                );

//         đoạn này là phải authenticated ( login) ms request đc vào system
        httpSecurity
                .oauth2ResourceServer(
                        oauth2 -> oauth2
                                .jwt(jwtConfigurer -> jwtConfigurer
                                        .decoder(customerJwtDecoder)
                                        )
                                //xử lý ngoại lệ khi người dùng chưa xác thực (unauthenticated) mà cố truy cập tài nguyên cần bảo vệ.
                                .authenticationEntryPoint(
                                        // file cấu hình xong
                                        new JwtAuthenticationEntryPoint() )
                );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
