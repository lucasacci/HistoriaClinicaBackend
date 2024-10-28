package com.ingsoft.tfi.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class MedicamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_medicamento;

    @Column
    private String nombreComercial;

    @Column
    private String nombreGenerico;

    private Integer cantidad;

    public MedicamentoModel(String nombreComercial, String nombreGenerico, Integer cantidad) {
        this.cantidad = cantidad;
        this.nombreComercial = nombreComercial;
        this.nombreGenerico = nombreGenerico;
    }
}
