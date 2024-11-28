package com.ingsoft.tfi.domain.models;

import com.ingsoft.tfi.domain.base.PersonaModel;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "medicos")
public class MedicoModel extends PersonaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_medico;

    @Column
    private String matricula;

    @Column
    private String especialidad;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "medico")
    private List<EvolucionModel> evoluciones;
//TODO: Agregar id_medico al constructor
    public MedicoModel(Long id_medico,
            String nombre,
                       String apellido,
                       String dni,
                       String email,
                       Date fechaNacimiento,
                       String direccion,
                       String telefono,
                       String matricula,
                       String especialidad) {
        super(nombre, apellido, dni, email, fechaNacimiento, direccion, telefono);
        this.matricula = matricula;
        this.especialidad = especialidad;
    }

    public MedicoModel() {
    }

    public Long getId_medico() {
        return id_medico;
    }

    public void setId_medico(Long id_medico) {
        this.id_medico = id_medico;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
