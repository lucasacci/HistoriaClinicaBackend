package com.ingsoft.tfi.services;

import com.ingsoft.tfi.models.PacienteModel;
import com.ingsoft.tfi.repositories.IRepositorioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PacienteService {

    @Autowired
    IRepositorioPaciente repositorioPaciente;

//    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//    Date fecha = sdf.parse("23/02/2000");

    private Map<String, PacienteModel> pacientes;


    public PacienteService() {
        pacientes = new HashMap<>(); // Inicialización en el constructor
    }


//    public PacienteService() throws ParseException {
//        pacientes = new HashMap<>();
//        //TODO: quitar esto cuando implemente la db
//
//        cargarPacientes();
//
//    }

    public Optional<PacienteModel> buscarPaciente(String dniPaciente){

        return repositorioPaciente.findByDni(dniPaciente);
    }

    public String agregarPaciente(PacienteModel paciente) {
        System.out.println(paciente.getDni());
        try {
            if (buscarPaciente(paciente.getDni()).isPresent()) {
                return "Paciente ya existente: " + paciente.getDni();
            } else {
                repositorioPaciente.save(paciente); // Guardar el paciente en la base de datos
                return "Paciente registrado: " + paciente.getDni();
            }
        } catch (Exception e) {
            return "Error al agregar el paciente: " + e.getMessage();
        }
    }




    public void actualizarPaciente(PacienteModel paciente) {
        repositorioPaciente.save(paciente); // Actualizar paciente en la base de datos
    }

//    private void cargarPacientes(){
//        pacientes.put("13232", new PacienteModel("daniel",
//                "osvaldo",
//                "12232",
//                "dsada",
//                fecha,
//                "colombia",
//                1234,
//                List.of("angina")
//                ));
//    }



}
