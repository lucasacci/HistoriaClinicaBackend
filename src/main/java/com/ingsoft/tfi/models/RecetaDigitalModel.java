package com.ingsoft.tfi.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "receta_digital")
public class RecetaDigitalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_receta_digital;

    @Column
    private Date fecha;

    @Column
    private String descripcion;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "id_receta_digital_detalle")
    private List<RecetaDigitalDetalleModel> recetaDigitaldetalle;

    public RecetaDigitalModel(Date fecha, String descripcion, List<RecetaDigitalDetalleModel> recetaDigitalDetalle) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.recetaDigitaldetalle = recetaDigitalDetalle;
    }

    public RecetaDigitalModel() {

    }

}