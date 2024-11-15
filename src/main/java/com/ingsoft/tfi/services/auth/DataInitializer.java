package com.ingsoft.tfi.services.auth;

import com.ingsoft.tfi.domain.models.auth.UserModel;
import com.ingsoft.tfi.repositories.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.findByUsername("admin").orElseGet(() -> {
                UserModel admin = new UserModel();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(UserModel.Role.ADMIN);
                return userRepository.save(admin);
            });
        };
    }
}
