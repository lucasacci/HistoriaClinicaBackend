package com.ingsoft.tfi.domain.models;

import jakarta.persistence.*;


@Entity
@Table(name = "medicamento")
public class MedicamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_medicamento;

    @Column
    private String nombreComercial;

    @Column
    private String nombreGenerico;

    @Column
    private String presentacion;

    @Column
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "id_receta_digital")
    private RecetaDigitalModel receta;

    public MedicamentoModel(String nombreComercial, String nombreGenerico, String presentacion, Integer cantidad) {
        this.nombreComercial = nombreComercial;
        this.nombreGenerico = nombreGenerico;
        this.presentacion = presentacion;
        this.cantidad = cantidad;
    }

    public MedicamentoModel(){}

    public Long getId_medicamento() {
        return id_medicamento;
    }

    public void setId_medicamento(Long id_medicamento) {
        this.id_medicamento = id_medicamento;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNombreGenerico() {
        return nombreGenerico;
    }

    public void setNombreGenerico(String nombreGenerico) {
        this.nombreGenerico = nombreGenerico;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public RecetaDigitalModel getReceta() {
        return receta;
    }

    public void setReceta(RecetaDigitalModel receta) {
        this.receta = receta;
    }
}
