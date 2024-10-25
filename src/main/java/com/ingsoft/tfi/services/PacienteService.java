package com.ingsoft.tfi.services;

import com.ingsoft.tfi.models.PacienteModel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PacienteService {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date fecha = sdf.parse("23/02/2000");

    private Map<String, PacienteModel> pacientes;

    public PacienteService() throws ParseException {
        pacientes = new HashMap<>();
        //TODO: quitar esto cuando implemente la db

        cargarPacientes();

    }

    public Optional<PacienteModel> buscarPaciente(String dniPaciente){

        return Optional.ofNullable(pacientes.get(dniPaciente));
    }

    public void actualizarPaciente(PacienteModel paciente) {
    }

    private void cargarPacientes(){
        pacientes.put("13232", new PacienteModel("daniel",
                "osvaldo",
                "12232",
                "dsada",
                fecha,
                "colombia",
                1234,
                List.of("angina")
                ));
    }

}
