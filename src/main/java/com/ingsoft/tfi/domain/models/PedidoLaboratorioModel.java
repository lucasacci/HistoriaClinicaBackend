package com.ingsoft.tfi.domain.models;

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
        if (descripcion == null || descripcion.trim().equals("")){
            throw new IllegalArgumentException("Pedido de laboratorio Nulo o Vacio.");
        }
        this.descripcion = descripcion;
    }

    public PedidoLaboratorioModel() {

    }

    public int getId_pedido_laboratorio() {
        return id_pedido_laboratorio;
    }

    public void setId_pedido_laboratorio(int id_pedido_laboratorio) {
        this.id_pedido_laboratorio = id_pedido_laboratorio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
