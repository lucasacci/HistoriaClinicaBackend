package com.ingsoft.tfi.models;

import jakarta.persistence.*;


@Entity
@Table(name = "medicamentos")
public class MedicamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_medicamento;

    @Column
    private String nombreComercial;

    @Column
    private String nombreGenerico;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_evolucion")
    private EvolucionModel evolucion;

    private Integer cantidad;

    public MedicamentoModel(String nombreComercial, String nombreGenerico, Integer cantidad) {
        this.cantidad = cantidad;
        this.nombreComercial = nombreComercial;
        this.nombreGenerico = nombreGenerico;
    }
}
