package com.ingsoft.tfi.services;

import com.ingsoft.tfi.domain.models.MedicamentoModel;
import com.ingsoft.tfi.repositories.IRepositorioMedicamento;

import java.util.Optional;

public class MedicamentoService {

   // @Autowired
    IRepositorioMedicamento repositorioMedicamento;

    public Optional<MedicamentoModel> buscarMedicamento(Long Id){
        return repositorioMedicamento.findById(Id);
    }

}
