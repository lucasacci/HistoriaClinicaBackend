package com.ingsoft.tfi.services;


import com.ingsoft.tfi.models.MedicoModel;
import com.ingsoft.tfi.models.PacienteModel;
import org.springframework.stereotype.Service;

@Service
public class SistemaClinica {

    private final PacienteService pacienteService;

    public SistemaClinica(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    public PacienteModel agregarEvolucion(MedicoModel medico, String dniPaciente, String diagnosticoElegido, String informe){
        PacienteModel paciente = pacienteService.buscarPaciente(dniPaciente).orElseThrow(() -> new RuntimeException("Paciente inexistente"));

        paciente.agregarEvolucion(diagnosticoElegido, medico, informe);
        pacienteService.actualizarPaciente(paciente);
        return paciente;
    }

    public PacienteModel buscarPaciente(String dniPaciente){
        return pacienteService.buscarPaciente(dniPaciente).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    public String agregarPaciente(PacienteModel paciente){
        return pacienteService.agregarPaciente(paciente);
    }
}
