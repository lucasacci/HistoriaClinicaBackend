package com.ingsoft.tfi.domain.models;

import com.ingsoft.tfi.domain.base.PersonaModel;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pacientes")
public class PacienteModel extends PersonaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id_paciente;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_historia_clinica")
    private HistoriaClinicaModel historiaClinica;

    public PacienteModel(String nombre, String apellido, String dni, String email, Date fechaNacimiento, String direccion, String telefono, Long id_paciente, HistoriaClinicaModel historiaClinica) {
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
                         String telefono,
                         List<String>diagnosticosPreexistentes) {
        super(nombre, apellido, dni, email, fechaNacimiento, direccion, telefono);
        this.historiaClinica = new HistoriaClinicaModel(diagnosticosPreexistentes);
    }

    public PacienteModel() {
        super();
    }

    public DiagnosticoModel buscarDiagnostico(Long idDiagnostico){
        return this.historiaClinica.buscarDiagnostico(idDiagnostico);
    }

    public void agregarEvolucion(Long diagnosticoElegido, MedicoModel medico, String informe,
                                 RecetaDigitalModel recetaDigital, PedidoLaboratorioModel pedidoLaboratorio){
        this.historiaClinica.agregarEvolucion(diagnosticoElegido, medico, informe, recetaDigital, pedidoLaboratorio);
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

    public void agregarDiagnostico(String diagnostico) {
        historiaClinica.agregarDiagnostico(diagnostico);
    }

    public void eliminarDiagnostico(Long idDiagnostico) {
        historiaClinica.eliminarDiagnostico(idDiagnostico);
    }
}
