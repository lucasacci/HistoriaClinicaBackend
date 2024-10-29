package com.ingsoft.tfi.services;

import com.ingsoft.tfi.models.DiagnosticoModel;
import com.ingsoft.tfi.models.EvolucionModel;
import com.ingsoft.tfi.models.HistoriaClinicaModel;
import com.ingsoft.tfi.models.PacienteModel;
import com.ingsoft.tfi.repositories.IRepositorioPaciente;
import jdk.jshell.Diag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PacienteService {

    @Autowired
    IRepositorioPaciente repositorioPaciente;

    private Map<String, PacienteModel> pacientes;


    public PacienteService() {
        pacientes = new HashMap<>();
    }

    public Optional<PacienteModel> buscarPaciente(String dniPaciente){
        return repositorioPaciente.findByDni(dniPaciente);
    }

    public String agregarPaciente(PacienteModel paciente) {
        try {
            if (buscarPaciente(paciente.getDni()).isPresent()) {
                return "Paciente ya existente, DNI: " + paciente.getDni();
            } else {
                repositorioPaciente.save(paciente);
                return "Paciente registrado, DNI: " + paciente.getDni();
            }
        } catch (Exception e) {
            return "Error al agregar el paciente: " + e.getMessage();
        }
    }

    public String borrarPaciente(String dniPaciente) {
        if (repositorioPaciente.findByDni(dniPaciente).isPresent()) {
            try{
                repositorioPaciente.deleteById(repositorioPaciente.findByDni(dniPaciente).get().id_paciente);
                return "Paciente eliminado, DNI: " + dniPaciente;
            } catch (Exception e) {
                return "Error al borrar el paciente, DNI: " + e.getMessage();
            }
        }else{
            return "Paciente no encontrado, DNI: " + dniPaciente;
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

    public List<PacienteModel> getPacientes() {
        List<PacienteModel> pacientes =  new ArrayList<>();
        repositorioPaciente.findAll().forEach(pacienteModel -> {pacientes.add(pacienteModel);});

        return pacientes;
    }
}
