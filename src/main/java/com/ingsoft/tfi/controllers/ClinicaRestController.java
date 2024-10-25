package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.models.MedicoModel;
import com.ingsoft.tfi.services.SistemaClinica;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class ClinicaRestController {

    private SistemaClinica sistemaClinica;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date fecha = sdf.parse("23/02/2000");
//TODO: Elimnar y usar la sesion para obtener el medico
    private final MedicoModel medicoPrueba = new MedicoModel("Lucho",
            "Casacci",
            "x",
            "olombiac",
            fecha,
            "m2a",
            38123,
        "232",
        "pediatra");

    public ClinicaRestController(SistemaClinica sistemaClinica) throws ParseException {
        this.sistemaClinica = sistemaClinica;
        System.out.println("ClinicaRestController inicializado");
    }


    @PostMapping("/paciente/{dniPaciente}/diagnostico/{nombreDiagnostico}/evolucion")
    public ResponseEntity<JsonNode> agregarEvolucion(@PathVariable String dniPaciente,
                                             @PathVariable String nombreDiagnostico,
                                             @RequestBody JsonNode json){

        var paciente = this.sistemaClinica.agregarEvolucion(medicoPrueba,
                dniPaciente,
                nombreDiagnostico,
                JsonParser.informeDesdeJson(json));

        return new ResponseEntity<>(JsonParser.pacienteAJson(paciente), HttpStatus.CREATED);
    }

    @GetMapping("/paciente/{dniPaciente}")
    public ResponseEntity<JsonNode> buscarPaciente(@PathVariable String dniPaciente){
        System.out.println(dniPaciente);
            var paciente = this.sistemaClinica.buscarPaciente(dniPaciente);
        System.out.println(paciente);
            return new ResponseEntity<>(JsonParser.pacienteAJson(paciente), HttpStatus.OK);

    }

}
