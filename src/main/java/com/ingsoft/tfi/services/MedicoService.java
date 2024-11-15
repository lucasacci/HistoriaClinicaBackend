package com.ingsoft.tfi.services;

import com.ingsoft.tfi.domain.models.MedicoModel;
import com.ingsoft.tfi.domain.models.PacienteModel;
import com.ingsoft.tfi.repositories.IMedicoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicoService {

    private IMedicoRepository repositorioMedico;


    public Optional<MedicoModel> buscarMedico(String dniMedico){
        return repositorioMedico.findByDni(dniMedico);
    }

    public String agregarMedico(MedicoModel medico) {
        try {
            if (buscarMedico(medico.getDni()).isPresent()) {
                return "Medico ya existente, DNI: " + medico.getDni();
            } else {
                repositorioMedico.save(medico);
                return "Medico registrado, DNI: " + medico.getDni();
            }
        } catch (Exception e) {
            return "Error al agregar el medico: " + e.getMessage();
        }
    }
}
