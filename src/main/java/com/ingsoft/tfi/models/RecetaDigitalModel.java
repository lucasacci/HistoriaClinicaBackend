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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "id_medicamentos")
    private List<MedicamentoModel> medicamentos;

    public RecetaDigitalModel(Date fecha, String descripcion, List<MedicamentoModel> medicamentos) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.medicamentos = medicamentos;
    }

    public RecetaDigitalModel() {

    }

}