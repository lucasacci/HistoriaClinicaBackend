package com.ingsoft.tfi.domain.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "id_receta_digital_detalle")
    private List<RecetaDigitalDetalleModel> recetaDigitaldetalle;

    public RecetaDigitalModel(Date fecha, String descripcion, List<MedicamentoModel> medicamentos, Map<String,Integer> medicamentosAmount) {
        if (descripcion == null || descripcion.trim().equals("")){
            throw new IllegalArgumentException("Descripcion de Receta Nula o Vacia.");
        }

        this.fecha = fecha;
        this.descripcion = descripcion;
        List<RecetaDigitalDetalleModel> recetaDigitaldetalle = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        medicamentos.forEach(medicamentoModel -> {
            recetaDigitaldetalle.add(new RecetaDigitalDetalleModel(
                    medicamentosAmount.get(
                            medicamentoModel.getNombreComercial()),
                            medicamentoModel,
                            this
                    )
            );
            counter.set(counter.get() + 1);
        });

        if(counter.get() >= 3) {
            throw new IllegalArgumentException("Se ingresaron mas de tres medicamentos a la Receta Digital.");
        }
        if(counter.get() == 0) {
            throw new IllegalArgumentException("No se ingresaron medicamentos en la Receta Digital.");
        }

        this.recetaDigitaldetalle = recetaDigitaldetalle;
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

    public List<RecetaDigitalDetalleModel> getRecetaDigitaldetalle() {
        return recetaDigitaldetalle;
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

    public void setRecetaDigitaldetalle(List<RecetaDigitalDetalleModel> recetaDigitaldetalle) {
        this.recetaDigitaldetalle = recetaDigitaldetalle;
    }
}