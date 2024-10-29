package com.ingsoft.tfi.models;

import jakarta.persistence.*;


@Entity
@Table(name = "medicamento")
public class MedicamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_medicamento;

    @Column
    private String nombreComercial;

    @Column
    private String nombreGenerico;

    public MedicamentoModel(String nombreComercial, String nombreGenerico) {
        this.nombreComercial = nombreComercial;
        this.nombreGenerico = nombreGenerico;
    }

    public MedicamentoModel(){}
}
