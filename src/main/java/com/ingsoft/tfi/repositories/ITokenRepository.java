package com.ingsoft.tfi.repositories;

import com.ingsoft.tfi.domain.auth.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {
}
