package com.ingsoft.tfi.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.domain.models.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SistemaClinica {

    private final PacienteService pacienteService;

    public SistemaClinica(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    public PacienteModel agregarEvolucion(MedicoModel medico, String dniPaciente, Long diagnosticoElegido, String informe,
                                          JsonNode recetaDigital, List<MedicamentoModel> medicamentos, String pedidoLaboratorio) {
        PacienteModel paciente = pacienteService.buscarPaciente(dniPaciente).orElseThrow(() -> new RuntimeException("Paciente inexistente"));

        paciente.agregarEvolucion(diagnosticoElegido, medico, informe, recetaDigital, medicamentos, pedidoLaboratorio);

        pacienteService.actualizarPaciente(paciente);
        return paciente;
    }

    public PacienteModel editarEvolucion(Long idEvolucion, MedicoModel medico, String dniPaciente, Long idDiagnostico, String informe,
                                         RecetaDigitalModel recetaDigital, PedidoLaboratorioModel pedidoLaboratorio) {
        PacienteModel paciente = pacienteService.buscarPaciente(dniPaciente).orElseThrow(() -> new RuntimeException("Paciente inexistente"));
        DiagnosticoModel diagnostico = paciente.buscarDiagnostico(idDiagnostico);
        diagnostico.editarEvolucion(idEvolucion, medico, informe, recetaDigital, pedidoLaboratorio);
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
        return pacienteService.buscarPaciente(paciente.getDni())
                .map(existingPaciente -> pacienteService.actualizarPaciente(paciente))
                .orElse("Paciente no encontrado");
    }

    public List<PacienteModel> getPacientes() {
        return pacienteService.getPacientes();
    }

    public void agregarDiagnostico(String dniPaciente, String diagnostico) {
        PacienteModel paciente = pacienteService.buscarPaciente(dniPaciente).orElseThrow(() -> new RuntimeException("Paciente inexistente"));

        paciente.agregarDiagnostico(diagnostico);

        pacienteService.actualizarPaciente(paciente);
    }

    public void editarDiagnostico(String dniPaciente, Long idDiagnostico, String descripcionDiagnostico) {
        PacienteModel paciente = pacienteService.buscarPaciente(dniPaciente).orElseThrow(() -> new RuntimeException("Paciente inexistente"));
        DiagnosticoModel diagnostico = paciente.buscarDiagnostico(idDiagnostico);
        diagnostico.editarDiagnostico(descripcionDiagnostico, diagnostico);
        pacienteService.actualizarPaciente(paciente);
    }

    public void eliminarEvolucion(String dniPaciente, Long idDiagnostico, Long idEvolucion) {
        PacienteModel paciente = pacienteService.buscarPaciente(dniPaciente).orElseThrow(() -> new RuntimeException("Paciente inexistente"));
        DiagnosticoModel diagnostico = paciente.buscarDiagnostico(idDiagnostico);
        diagnostico.eliminarEvolucion(idEvolucion);
        pacienteService.actualizarPaciente(paciente);
    }

    public void eliminarDiagnostico(String dniPaciente, Long idDiagnostico) {
        PacienteModel paciente = pacienteService.buscarPaciente(dniPaciente).orElseThrow(() -> new RuntimeException("Paciente inexistente"));
        paciente.eliminarDiagnostico(idDiagnostico);
        pacienteService.actualizarPaciente(paciente);
    }
}
