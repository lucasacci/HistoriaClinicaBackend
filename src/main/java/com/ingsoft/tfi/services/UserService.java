package com.ingsoft.tfi.services;

import com.ingsoft.tfi.domain.models.UserModel;
import com.ingsoft.tfi.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository; // Repositorio para interactuar con la base de datos

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean validateUser(String email, String password) {
        try{
            Optional<UserModel> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                return passwordEncoder.matches(password, user.get().getPassword());
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void createTestUser() {
        if (!userRepository.findByEmail("testuser@example.com").isPresent()) {
            UserModel testUser = new UserModel();
            testUser.setEmail("testuser@example.com");
            testUser.setPassword(passwordEncoder.encode("testpassword"));
            userRepository.save(testUser);
        }
    }
}
