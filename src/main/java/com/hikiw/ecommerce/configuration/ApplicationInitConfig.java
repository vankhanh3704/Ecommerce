package com.hikiw.ecommerce.configuration;


import com.hikiw.ecommerce.Entity.RoleEntity;
import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Enum.Role;
import com.hikiw.ecommerce.Repository.RoleRepository;
import com.hikiw.ecommerce.Repository.UserRepository;
import com.hikiw.ecommerce.constant.PredefinedRole;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";


    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if(userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()){
                RoleEntity adminRole = roleRepository.save(RoleEntity.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Quản trị viên")
                        .build());
                var roles = new HashSet<RoleEntity>();
                roles.add(adminRole);
                UserEntity userEntity = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();
                userRepository.save(userEntity);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}
