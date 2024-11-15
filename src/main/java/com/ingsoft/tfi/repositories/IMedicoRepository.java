package com.ingsoft.tfi.repositories;

import com.ingsoft.tfi.domain.models.MedicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IMedicoRepository extends JpaRepository <MedicoModel, Long> {
    Optional<MedicoModel> findByDni(String dniMedico);
}
