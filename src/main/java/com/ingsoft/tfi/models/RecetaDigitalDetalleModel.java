package com.ingsoft.tfi.models;

import jakarta.persistence.*;

@Entity
@Table(name = "receta_digital_detalle")
public class RecetaDigitalDetalleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_receta_digital_detalle;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_receta_digital")
    private RecetaDigitalModel recetaDigital;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_medicamento")
    private MedicamentoModel medicamento;

    @Column
    private Integer cantidad;

    public RecetaDigitalDetalleModel(Integer cantidad, MedicamentoModel medicamento){
        this.medicamento = medicamento;
        this.cantidad = cantidad;
    }

    public RecetaDigitalDetalleModel() {}
}