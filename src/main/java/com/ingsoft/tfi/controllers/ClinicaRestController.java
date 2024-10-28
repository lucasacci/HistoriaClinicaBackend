package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.models.MedicoModel;
import com.ingsoft.tfi.models.PacienteModel;
import com.ingsoft.tfi.services.SistemaClinica;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ClinicaRestController {

    private SistemaClinica sistemaClinica;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date fecha = sdf.parse("23/02/2000");
//TODO: Elimnar y usar la sesion para obtener el medico
    private final MedicoModel medicoPrueba = new MedicoModel(1l,"Lucho",
            "Casacci",
            "x",
            "colombia",
            fecha,
            "m2a",
            38123,
        "232",
        "pediatra");

    public ClinicaRestController(SistemaClinica sistemaClinica) throws ParseException {
        this.sistemaClinica = sistemaClinica;
        System.out.println("ClinicaRestController inicializado");
    }


    @PostMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}/evolucion")
    public ResponseEntity<JsonNode> agregarEvolucion(@PathVariable String dniPaciente,
                                             @PathVariable Long idDiagnostico,
                                             @RequestBody JsonNode json){

        var paciente = this.sistemaClinica.agregarEvolucion(medicoPrueba,
                dniPaciente,
                idDiagnostico,
                JsonParser.informeDesdeJson(json),
                Optional.of(JsonParser.recetaDigitalDesdeJson(json)));
        return new ResponseEntity<>(JsonParser.pacienteAJson(paciente), HttpStatus.CREATED);
    }

    @GetMapping("/paciente/{dniPaciente}")
    public ResponseEntity<JsonNode> buscarPaciente(@PathVariable String dniPaciente){
            var paciente = this.sistemaClinica.buscarPaciente(dniPaciente);
        System.out.println(paciente);
            return new ResponseEntity<>(JsonParser.pacienteAJson(paciente), HttpStatus.OK);
    }
//
//    @GetMapping("/pacientes")
//    public ResponseEntity<JsonNode> buscarPacientes() {
//        List<PacienteModel> pacientes = JsonParser.pacienteDesdeJson()
//
//        return
//    }

    @PostMapping("/paciente")
    public ResponseEntity<String> agregarPaciente(@RequestBody JsonNode jsonPaciente) {
        PacienteModel paciente = JsonParser.pacienteDesdeJson(jsonPaciente);

        String respuesta = sistemaClinica.agregarPaciente(paciente);

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @DeleteMapping("/paciente/{dniPaciente}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable String dniPaciente){
            String resultado = sistemaClinica.borrarPaciente(dniPaciente);

            return ResponseEntity.status(HttpStatus.OK).body(resultado);

    }

    @PutMapping("/paciente/{idPaciente}")
    public ResponseEntity<String> editarPaciente(@PathVariable String idPaciente, @RequestBody JsonNode jsonPaciente){
        PacienteModel paciente = JsonParser.pacienteDesdeJson(jsonPaciente);

        String respuesta = sistemaClinica.editarPaciente(paciente);

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }


}
