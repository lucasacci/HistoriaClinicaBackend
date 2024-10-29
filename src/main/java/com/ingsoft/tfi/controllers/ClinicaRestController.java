package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.models.ApiResponse;
import com.ingsoft.tfi.models.MedicoModel;
import com.ingsoft.tfi.models.PacienteModel;
import com.ingsoft.tfi.services.SistemaClinica;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    }


    @PostMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}/evolucion")
    public ResponseEntity<ApiResponse<?>> agregarEvolucion(@PathVariable String dniPaciente,
                                             @PathVariable Long idDiagnostico,
                                             @RequestBody JsonNode json){

        try {
            var paciente = this.sistemaClinica.agregarEvolucion(
                    medicoPrueba,
                    dniPaciente,
                    idDiagnostico,
                    JsonParser.informeDesdeJson(json),
                    JsonParser.recetaDigitalDesdeJson(json)
            );
            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                    "Evolución agregada exitosamente.",
                    JsonParser.pacienteAJson(paciente));

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Error al agregar la evolución: " + e.getMessage(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

//    @DeleteMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}/evolucion")
//    public ResponseEntity<JsonNode> eliminarEvolucion(@PathVariable String dniPaciente,
//                                                     @PathVariable Long idDiagnostico,
//                                                     @RequestBody JsonNode json){
//
//        var paciente = this.sistemaClinica.eliminarEvolucion(medicoPrueba,
//                dniPaciente,
//                idDiagnostico,
//                JsonParser.informeDesdeJson(json));
//        return new ResponseEntity<>(JsonParser.pacienteAJson(paciente), HttpStatus.CREATED);
//    }

    @GetMapping("/paciente")
    public ResponseEntity<ApiResponse<?>> getPacientes(){
        List<PacienteModel> pacientes = sistemaClinica.getPacientes();
        List<JsonNode> pacientesJson = new ArrayList<>();


        try{
            pacientes.forEach(pacienteModel -> {pacientesJson.add(JsonParser.pacienteAJson(pacienteModel));});
            ApiResponse<List<JsonNode>> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                    "Obtener pacientes",
                    pacientesJson);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Error al obtener pacientes: " + e.getMessage(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/paciente/{dniPaciente}")
    public ResponseEntity<ApiResponse<?>> buscarPaciente(@PathVariable String dniPaciente){

        try{
            var paciente = this.sistemaClinica.buscarPaciente(dniPaciente);

            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.FOUND.value(),
                    "Paciente encontrado",
                    JsonParser.pacienteAJson(paciente));

            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                    "Error al buscar el paciente: " + e.getMessage(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
//
//    @GetMapping("/pacientes")
//    public ResponseEntity<JsonNode> buscarPacientes() {
//        List<PacienteModel> pacientes = JsonParser.pacienteDesdeJson()
//
//        return
//    }

    @PostMapping("/paciente")
    public ResponseEntity<ApiResponse<?>> agregarPaciente(@RequestBody JsonNode jsonPaciente) {


        try{
            PacienteModel paciente = JsonParser.pacienteDesdeJson(jsonPaciente);
            String respuesta = sistemaClinica.agregarPaciente(paciente);

            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                     respuesta,
                    JsonParser.pacienteAJson(paciente));

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {

            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al agregar el paciente: " + e.getMessage(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/paciente/{dniPaciente}")
    public ResponseEntity<ApiResponse<String>> eliminarPaciente(@PathVariable String dniPaciente){

            try{
                String resultado = sistemaClinica.borrarPaciente(dniPaciente);

                ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(),
                        resultado,
                        null);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception e) {
                ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(),
                        "Error al borrar paciente: " + e.getMessage(),
                        null);

                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @PutMapping("/paciente/{idPaciente}")
    public ResponseEntity<ApiResponse<String>> editarPaciente(@PathVariable String idPaciente, @RequestBody JsonNode jsonPaciente) {
        try {
            PacienteModel paciente = JsonParser.pacienteDesdeJson(jsonPaciente);

            if (!paciente.getDni().equals(idPaciente)) {
                ApiResponse<String> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                        "El ID del paciente en la URL no coincide con el ID en los datos proporcionados.",
                        null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            String respuesta = sistemaClinica.editarPaciente(paciente);

            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(),
                    respuesta,
                    null);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al editar paciente: " + e.getMessage(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
