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

    public int getId_medicamento() {
        return id_medicamento;
    }

    public void setId_medicamento(int id_medicamento) {
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
}
