package com.ingsoft.tfi.services;


import com.ingsoft.tfi.models.MedicoModel;
import com.ingsoft.tfi.models.PacienteModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SistemaClinica {

    private final PacienteService pacienteService;

    public SistemaClinica(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    public PacienteModel agregarEvolucion(MedicoModel medico, String dniPaciente, Long diagnosticoElegido, String informe){
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
    public String borrarPaciente(String dniPaciente){
        return pacienteService.borrarPaciente(dniPaciente);
    }

    public String editarPaciente(PacienteModel paciente) {
        return pacienteService.actualizarPaciente(paciente);
    }

}
