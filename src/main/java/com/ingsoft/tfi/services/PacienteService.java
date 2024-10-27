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
        pacientes = new HashMap<>();
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
        try {
            if (buscarPaciente(paciente.getDni()).isPresent()) {
                return "Paciente ya existente: " + paciente.getDni();
            } else {
                repositorioPaciente.save(paciente);
                return "Paciente registrado: " + paciente.getDni();
            }
        } catch (Exception e) {
            return "Error al agregar el paciente: " + e.getMessage();
        }
    }

    public String borrarPaciente(String dniPaciente) {
        if (repositorioPaciente.findByDni(dniPaciente).isPresent()) {
            try{
                repositorioPaciente.deleteById(repositorioPaciente.findByDni(dniPaciente).get().id_paciente);
                return "Paciente eliminado: " + dniPaciente;
            } catch (Exception e) {
                return "Error al borrar el paciente: " + e.getMessage();
            }
        }else{
            return "Paciente no encontrado: " + dniPaciente;
        }
    }




    public String actualizarPaciente(PacienteModel paciente) {
        PacienteModel pacienteViejo = repositorioPaciente.findByDni(paciente.getDni()).get();

        pacienteViejo.setNombre(paciente.getNombre());
        pacienteViejo.setApellido(paciente.getApellido());
        pacienteViejo.setEmail(paciente.getEmail());
        pacienteViejo.setDni(paciente.getDni());
        pacienteViejo.setFechaNacimiento(paciente.getFechaNacimiento());
        pacienteViejo.setDireccion(paciente.getDireccion());
        pacienteViejo.setTelefono(paciente.getTelefono());

        repositorioPaciente.save(pacienteViejo);

        return "Paciente actualizado: " + paciente.getDni();

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
