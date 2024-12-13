package com.ingsoft.tfi.domain.models;

import jakarta.persistence.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "receta")
    private List<MedicamentoModel> medicamentos;

    public RecetaDigitalModel(Date fecha, String descripcion, List<MedicamentoModel> medicamentos) {
        if (descripcion == null || descripcion.trim().equals("")){
            throw new IllegalArgumentException("Descripcion de Receta Nula o Vacia.");
        }

        this.fecha = fecha;
        this.descripcion = descripcion;

        var posiblesMedicamentos = Optional.ofNullable(medicamentos);

        if (posiblesMedicamentos.isEmpty()){
            throw new IllegalArgumentException("Medicamentos nulos o vacios");
        }

        int counter = medicamentos.size();

        if(counter >= 3) {
            throw new IllegalArgumentException("Se ingresaron mas de tres medicamentos a la Receta Digital.");
        }
        if(counter == 0) {
            throw new IllegalArgumentException("No se ingresaron medicamentos en la Receta Digital.");
        }
        medicamentos.forEach(medicamentoModel -> medicamentoModel.setReceta(this));
        this.medicamentos = medicamentos;
    }


    public RecetaDigitalModel() {

    }

    public int getId_receta_digital() {
        return id_receta_digital;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }


    public void setId_receta_digital(int id_receta_digital) {
        this.id_receta_digital = id_receta_digital;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<MedicamentoModel> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<MedicamentoModel> medicamentos) {
        this.medicamentos = medicamentos;
    }
}