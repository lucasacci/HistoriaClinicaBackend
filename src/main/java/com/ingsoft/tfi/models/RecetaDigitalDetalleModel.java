package com.ingsoft.tfi.models;

import jakarta.persistence.*;

@Entity
@Table(name = "receta_digital_detalle")
public class RecetaDigitalDetalleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_receta_digital_detalle;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_receta_digital")
    private RecetaDigitalModel recetaDigital;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_medicamento")
    private MedicamentoModel medicamento;

    @Column
    private Integer cantidad;

    public RecetaDigitalDetalleModel(Integer cantidad, MedicamentoModel medicamento, RecetaDigitalModel recetaDigital){
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.recetaDigital = recetaDigital;
    }

    public RecetaDigitalDetalleModel() {}

    public int getId_receta_digital_detalle() {
        return id_receta_digital_detalle;
    }

    public void setId_receta_digital_detalle(int id_receta_digital_detalle) {
        this.id_receta_digital_detalle = id_receta_digital_detalle;
    }

    public RecetaDigitalModel getRecetaDigital() {
        return recetaDigital;
    }

    public void setRecetaDigital(RecetaDigitalModel recetaDigital) {
        this.recetaDigital = recetaDigital;
    }

    public MedicamentoModel getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(MedicamentoModel medicamento) {
        this.medicamento = medicamento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}