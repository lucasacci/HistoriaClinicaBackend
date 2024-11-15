package com.ingsoft.tfi.repositories;

import com.ingsoft.tfi.domain.models.PacienteModel;
import com.ingsoft.tfi.domain.models.auth.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
}
