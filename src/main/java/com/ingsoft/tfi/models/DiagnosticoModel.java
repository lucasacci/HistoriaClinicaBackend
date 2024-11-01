package com.ingsoft.tfi.models;

import jakarta.persistence.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "diagnostico", orphanRemoval = true)
    private List<EvolucionModel> evoluciones;

    public DiagnosticoModel(String descripcion) {
        this.descripcion = descripcion;
       this.evoluciones = new ArrayList<EvolucionModel>();
    }

    public DiagnosticoModel() {
        this.evoluciones = new ArrayList<>(); // Inicialización para evitar null
    }

    public boolean existeDiagnostico(Long id) {
        return this.id.equals(id);
    }

//    public boolean tieneEvolucion(MedicoModel medico, String informe){
//        return evoluciones.stream().anyMatch(evolucion -> evolucion.tiene(medico, informe));
//    }



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



    public void editarDiagnostico(String descripcionDiagnostico, DiagnosticoModel diagnostico) {
        diagnostico.setDescripcion(descripcionDiagnostico);
    }

    public void agregarEvolucion(MedicoModel medico, String informe,
                                 RecetaDigitalModel recetaDigital, PedidoLaboratorioModel pedidoLaboratorio) {
        Timestamp fechaEvolucion = new Timestamp(new Date().getTime());
        EvolucionModel evolucion = new EvolucionModel(informe, fechaEvolucion, medico, recetaDigital, pedidoLaboratorio);
        evolucion.setDiagnostico(this);
        evoluciones.add(evolucion);
    }

    public void editarEvolucion(Long idEvolucion, MedicoModel medico, String informe,
                                RecetaDigitalModel recetaDigital, PedidoLaboratorioModel pedidoLaboratorio) {
        Timestamp fechaEdicionEvolucion = new Timestamp(new Date().getTime());
        EvolucionModel evolucion = evoluciones.stream()
                .filter(evolucionModel -> evolucionModel.getId_evolucion().equals(idEvolucion))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Evolución no encontrada"));

        evolucion.setDiagnostico(this);

        if(recetaDigital != null && pedidoLaboratorio != null) {
            throw new IllegalArgumentException("No se puede crear una evolución con receta y con pedido de laboratorio");
        }

        if(evolucion.getRecetaDigital() == null && evolucion.getPedidoLaboratorio() == null){
            if(recetaDigital != null && pedidoLaboratorio == null) {
                evolucion.setRecetaDigital(recetaDigital);
            } else if(pedidoLaboratorio != null && recetaDigital == null) {
                evolucion.setPedidoLaboratorio(pedidoLaboratorio);
            }
        }

        if(evolucion.getPedidoLaboratorio() != null && recetaDigital != null) {
            evolucion.setRecetaDigital(recetaDigital);
            evolucion.setPedidoLaboratorio(null);
        }else if (evolucion.getRecetaDigital() != null && pedidoLaboratorio != null) {
            evolucion.setPedidoLaboratorio(pedidoLaboratorio);
            evolucion.setRecetaDigital(null);
        }

        if(evolucion.getPedidoLaboratorio() != null && pedidoLaboratorio == null){
            evolucion.setPedidoLaboratorio(null);
        }else if (evolucion.getRecetaDigital() != null && recetaDigital == null) {
            evolucion.setRecetaDigital(null);
        }

        evolucion.setFecha(fechaEdicionEvolucion);
        evolucion.setMedico(medico);
        evolucion.setInforme(informe);
    }

    public void eliminarEvolucion(Long idEvolucion) {
        EvolucionModel evolucion = evoluciones.stream()
                .filter(evolucionModel -> evolucionModel.getId_evolucion().equals(idEvolucion))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Evolución no encontrada"));

        evoluciones.remove(evolucion);
    }

}
