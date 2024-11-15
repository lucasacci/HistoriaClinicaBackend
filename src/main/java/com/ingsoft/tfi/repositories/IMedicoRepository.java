package com.ingsoft.tfi.repositories;

import com.ingsoft.tfi.domain.models.MedicoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMedicoRepository extends JpaRepository <MedicoModel, Long> {
}
