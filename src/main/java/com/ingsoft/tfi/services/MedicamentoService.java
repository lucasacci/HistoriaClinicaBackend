package com.ingsoft.tfi.services;

import com.ingsoft.tfi.models.MedicamentoModel;
import com.ingsoft.tfi.models.PacienteModel;
import com.ingsoft.tfi.repositories.IRepositorioMedicamento;
import com.ingsoft.tfi.repositories.IRepositorioPaciente;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class MedicamentoService {

   // @Autowired
    IRepositorioMedicamento repositorioMedicamento;

    public Optional<MedicamentoModel> buscarMedicamento(Long Id){
        return repositorioMedicamento.findById(Id);
    }

}
