package com.ingsoft.tfi.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pacientes")
public class PacienteModel extends PersonaModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id_paciente;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_historia_clinica")
    private HistoriaClinicaModel historiaClinica;

    public PacienteModel(String nombre, String apellido, String dni, String email, Date fechaNacimiento, String direccion, int telefono, Long id_paciente, HistoriaClinicaModel historiaClinica) {
        super(nombre, apellido, dni, email, fechaNacimiento, direccion, telefono);
        this.id_paciente = id_paciente;
        this.historiaClinica = historiaClinica;
    }

    public PacienteModel(String nombre,
                         String apellido,
                         String dni,
                         String email,
                         Date fechaNacimiento,
                         String direccion,
                         int telefono,
                         List<String>diagnosticosPreexistentes) {
        super(nombre, apellido, dni, email, fechaNacimiento, direccion, telefono);
        this.historiaClinica = new HistoriaClinicaModel(diagnosticosPreexistentes);
    }

    public PacienteModel() {
        super();
    }

    public DiagnosticoModel buscarDiagnostico(String nombreDiagnostico){
        return this.historiaClinica.buscarDiagnostico(nombreDiagnostico);
    }

    public void agregarEvolucion(String diagnosticoElegido, MedicoModel medico, String informe){
        this.historiaClinica.agregarEvolucion(diagnosticoElegido, medico, informe);
    }

    public Long getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(Long id_paciente) {
        this.id_paciente = id_paciente;
    }

    public HistoriaClinicaModel getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinicaModel historiaClinica) {
        this.historiaClinica = historiaClinica;
    }
}
