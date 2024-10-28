package com.ingsoft.tfi.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "evoluciones")
public class EvolucionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id_evolucion;

    @Column
    public Date fecha;
    @Column
    public String informe;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_diagnostico")
    private DiagnosticoModel diagnostico;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_medico")
    private MedicoModel medico;

    public EvolucionModel(String informe, Date fecha, MedicoModel medico) {
        this.informe = informe;
        this.fecha = fecha;
        this.medico = medico;
    }

    public EvolucionModel() {

    }

    public boolean tiene(MedicoModel medico, String informe){
        return this.informe.equals(informe) && this.medico.equals(medico);
    }

    public DiagnosticoModel getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(DiagnosticoModel diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Long getId_evolucion() {
        return id_evolucion;
    }

    public void setId_evolucion(Long id_evolucion) {
        this.id_evolucion = id_evolucion;
    }

    public MedicoModel getMedico() {
        return medico;
    }

    public void setMedico(MedicoModel medico) {
        this.medico = medico;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
