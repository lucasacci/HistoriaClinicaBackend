package com.ingsoft.tfi.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pedido_laboratorio")

public class PedidoLaboratorioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pedido_laboratorio;

    @Column
    private String descripcion;

    public PedidoLaboratorioModel(String descripcion) {
        this.descripcion = descripcion;
    }

    public PedidoLaboratorioModel() {

    }
}
