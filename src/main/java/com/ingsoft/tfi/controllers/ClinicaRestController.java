package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.dto.ApiResponse;
import com.ingsoft.tfi.domain.models.MedicoModel;
import com.ingsoft.tfi.domain.models.PacienteModel;
import com.ingsoft.tfi.helpers.JsonParser;
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
            "38123",
        "232",
        "pediatra");

    public ClinicaRestController(SistemaClinica sistemaClinica) throws ParseException {
        this.sistemaClinica = sistemaClinica;
    }


    @PostMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}/evolucion")
    public ResponseEntity<JsonNode> agregarEvolucion(@PathVariable String dniPaciente,
                                             @PathVariable Long idDiagnostico,
                                             @RequestBody JsonNode json){
        try {
            var paciente = this.sistemaClinica.agregarEvolucion(
                    medicoPrueba,
                    dniPaciente,
                    idDiagnostico,
                    JsonParser.informeDesdeJson(json),
                    JsonParser.checkRecetaDigiatal(json),
                    JsonParser.getMedicamentosFromJson(json),
                    JsonParser.getMedicamentosAmountFromJson(json),
                    JsonParser.pedidoLaboratorioDescriptionFromJson(json)
            );
            paciente.getHistoriaClinica().getDiagnosticos().forEach(e->
                    e.getEvoluciones().forEach(x ->
                            System.out.println(x.getFecha())
                    )
            );
            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Evolución agregada exitosamente.",
                    JsonParser.pacienteAJson(paciente));

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Error al agregar la evolución. Ex: " + e.getMessage(),
                    null
            );

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}/evolucion/{idEvolucion}")
    public ResponseEntity<JsonNode> editarEvolucion(@PathVariable String dniPaciente,
                                                    @PathVariable Long idDiagnostico,
                                                    @PathVariable Long idEvolucion,
                                                    @RequestBody JsonNode json){
        try {

            var paciente = this.sistemaClinica.editarEvolucion(
                    idEvolucion,
                    medicoPrueba,
                    dniPaciente,
                    idDiagnostico,
                    JsonParser.informeDesdeJson(json),
                    JsonParser.recetaDigitalDesdeJson(json),
                    JsonParser.pedidoLaboratorioDesdeJson(json)
            );

            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Evolucion editada exitosamente.",
                    JsonParser.pacienteAJson(paciente));

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<JsonNode>> response = new ApiResponse<>(
                    "Error al editar evolucion: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}/evolucion/{idEvolucion}")
    public ResponseEntity<JsonNode> eliminarEvolucion(@PathVariable String dniPaciente,
                                                     @PathVariable Long idDiagnostico,
                                                     @PathVariable Long idEvolucion){

        try {
                this.sistemaClinica.eliminarEvolucion(dniPaciente,
                    idDiagnostico,
                    idEvolucion);

            ApiResponse<List<JsonNode>> response = new ApiResponse<>(
                    "Evolucion eliminada exitosamente.",
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<JsonNode>> response = new ApiResponse<>(
                    "Error al eliminar evolucion: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/paciente/{dniPaciente}/diagnostico")
    public ResponseEntity<JsonNode> agregarDiagnostico(@PathVariable String dniPaciente, @RequestBody JsonNode json) {
        try {
            String descripcionDiagnostico = JsonParser.diagnosticoDesdeJson(json);

            sistemaClinica.agregarDiagnostico(dniPaciente, descripcionDiagnostico);

            ApiResponse<List<JsonNode>> response = new ApiResponse<>(
                    "Diagnóstico agregado exitosamente.",
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {

            ApiResponse<List<JsonNode>> response = new ApiResponse<>(
                    "Error al agregar diagnóstico: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}")
    public ResponseEntity<JsonNode> editarDiagnostico(@PathVariable String dniPaciente, @PathVariable Long idDiagnostico, @RequestBody JsonNode json) {
        try {
            String descripcionDiagnostico = JsonParser.diagnosticoDesdeJson(json);

            sistemaClinica.editarDiagnostico(dniPaciente, idDiagnostico, descripcionDiagnostico);

            ApiResponse<List<JsonNode>> response = new ApiResponse<>(
                    "Diagnóstico editado exitosamente.",
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<JsonNode>> response = new ApiResponse<>(
                    "Error al editar diagnóstico: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}")
    public ResponseEntity<JsonNode> eliminarDiagnostico(@PathVariable String dniPaciente, @PathVariable Long idDiagnostico) {
        try {

            sistemaClinica.eliminarDiagnostico(dniPaciente, idDiagnostico);

            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Diagnóstico eliminado exitosamente.",
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Error al eliminar diagnóstico: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/paciente")
    public ResponseEntity<JsonNode> getPacientes() {
        List<PacienteModel> pacientes = sistemaClinica.getPacientes();
        List<JsonNode> pacientesJson = new ArrayList<>();

        try {
            pacientes.forEach(pacienteModel -> pacientesJson.add(JsonParser.pacienteAJson(pacienteModel)));

            ApiResponse<List<JsonNode>> response = new ApiResponse<>(
                    "Obtener pacientes",
                    pacientesJson);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    "Error al obtener pacientes: " + e.getMessage(),
                    null);

            JsonNode errorResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/paciente/{dniPaciente}")
    public ResponseEntity<JsonNode> buscarPaciente(@PathVariable String dniPaciente){

        try{
            var paciente = this.sistemaClinica.buscarPaciente(dniPaciente);

            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Paciente encontrado",
                    JsonParser.pacienteAJson(paciente));

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.FOUND);
        } catch (Exception e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Error al buscar el paciente: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/paciente")
    public ResponseEntity<JsonNode> agregarPaciente(@RequestBody JsonNode jsonPaciente) {


        try{
            PacienteModel paciente = JsonParser.pacienteDesdeJson(jsonPaciente);
            String respuesta = sistemaClinica.agregarPaciente(paciente);

            ApiResponse<JsonNode> response = new ApiResponse<>(
                    respuesta,
                    JsonParser.pacienteAJson(paciente));

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
        } catch (Exception e) {

            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Error al agregar el paciente: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/paciente/{dniPaciente}")
    public ResponseEntity<JsonNode> eliminarPaciente(@PathVariable String dniPaciente){

            try{
                String resultado = sistemaClinica.borrarPaciente(dniPaciente);

                ApiResponse<JsonNode> response = new ApiResponse<>(
                        resultado,
                        null);

                JsonNode jsonResponse = JsonParser.responseAJson(response);

                return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
            } catch (Exception e) {
                ApiResponse<String> response = new ApiResponse<>(
                        "Error al borrar paciente: " + e.getMessage(),
                        null);

                JsonNode jsonResponse = JsonParser.responseAJson(response);

                return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @PutMapping("/paciente/{idPaciente}")
    public ResponseEntity<JsonNode> editarPaciente(@PathVariable String idPaciente, @RequestBody JsonNode jsonPaciente) {
        try {
            PacienteModel paciente = JsonParser.pacienteDesdeJson(jsonPaciente);

            if (!paciente.getDni().equals(idPaciente)) {
                ApiResponse<JsonNode> response = new ApiResponse<>(
                        "El ID del paciente en la URL no coincide con el ID en los datos proporcionados.",
                        null);

                JsonNode jsonResponse = JsonParser.responseAJson(response);

                return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
            }

            String respuesta = sistemaClinica.editarPaciente(paciente);

            ApiResponse<JsonNode> response = new ApiResponse<>(
                    respuesta,
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Error al editar paciente: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
