package com.ingsoft.tfi.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "diagnosticos")
public class DiagnosticoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String descripcion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_historia_clinica")
    private HistoriaClinicaModel historiaClinica;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "diagnostico")
    private List<EvolucionModel> evoluciones;

    public DiagnosticoModel(String descripcion) {
        this.descripcion = descripcion;
       this.evoluciones = new ArrayList<EvolucionModel>();
    }

    public DiagnosticoModel() {

    }

    public boolean existeDiagnostico(Long id) {
        return this.id.equals(id);
    }

    public boolean tieneEvolucion(MedicoModel medico, String informe){
        return evoluciones.stream().anyMatch(evolucion -> evolucion.tiene(medico, informe));
    }

    public void agregarEvolucion(MedicoModel medico, String informe) {
        Timestamp fechaEvolucion = new Timestamp(new Date().getTime());
        EvolucionModel evolucion = new EvolucionModel(informe, fechaEvolucion, medico);

        evoluciones.add(evolucion);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HistoriaClinicaModel getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinicaModel historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<EvolucionModel> getEvoluciones() {
        return evoluciones;
    }

    public void setEvoluciones(List<EvolucionModel> evoluciones) {
        this.evoluciones = evoluciones;
    }
}
