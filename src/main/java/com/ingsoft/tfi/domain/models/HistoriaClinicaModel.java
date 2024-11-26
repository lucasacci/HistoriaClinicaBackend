package com.ingsoft.tfi.domain.models;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "historias_clinicas")
public class HistoriaClinicaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_historia_clinica;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_paciente")
    private PacienteModel paciente;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "historiaClinica", orphanRemoval = true)
    private List<DiagnosticoModel> diagnosticos;


    public HistoriaClinicaModel(List<String> diagnosticos) {
        this.diagnosticos = diagnosticos.stream()
                .map(nombreDiagnostico -> new DiagnosticoModel(nombreDiagnostico))
                .collect(Collectors.toList());
    }

    public HistoriaClinicaModel() {

    }

    public void agregarDiagnostico(String diagnostico) {
        DiagnosticoModel diagnosticoNuevo = new DiagnosticoModel(diagnostico);
        diagnosticoNuevo.setEvoluciones(new ArrayList<>());
        diagnosticos.add(diagnosticoNuevo);
        diagnosticoNuevo.setHistoriaClinica(this);
    }

    public DiagnosticoModel buscarDiagnostico(Long idDiagnostico) {
        return this.diagnosticos.stream()
                .filter(diagnostico -> diagnostico.existeDiagnostico(idDiagnostico))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("diagnostico no encontrado"));
    }

    public void agregarEvolucion(Long diagnosticoElegido, MedicoModel medico, String informe,
                                 JsonNode recetaDigital, List<MedicamentoModel> medicamentos, Map<String,Integer> medicamentosAmount, String pedidoLaboratorio) {
        DiagnosticoModel diagnostico = buscarDiagnostico(diagnosticoElegido);
        if (diagnostico != null) {
            diagnostico.agregarEvolucion(medico, informe, recetaDigital, medicamentos, medicamentosAmount, pedidoLaboratorio);
        } else {
            throw new RuntimeException("Diagnóstico no encontrado");
        }
    }

    public int getId_historia_clinica() {
        return id_historia_clinica;
    }

    public void setId_historia_clinica(int id_historia_clinica) {
        this.id_historia_clinica = id_historia_clinica;
    }

    public List<DiagnosticoModel> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(List<DiagnosticoModel> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public PacienteModel getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteModel paciente) {
        this.paciente = paciente;
    }

    public void eliminarDiagnostico(Long idDiagnostico) {
        DiagnosticoModel diagnostico = buscarDiagnostico(idDiagnostico);
        diagnosticos.remove(diagnostico);
    }
}
