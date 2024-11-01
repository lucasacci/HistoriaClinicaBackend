package com.ingsoft.tfi.repositories;

import com.ingsoft.tfi.domain.models.MedicamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRepositorioMedicamento extends JpaRepository<MedicamentoModel, Long> {
    Optional<MedicamentoModel> findById(Long Id);
}