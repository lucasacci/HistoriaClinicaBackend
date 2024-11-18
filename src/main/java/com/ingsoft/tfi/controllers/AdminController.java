package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.domain.models.MedicoModel;
import com.ingsoft.tfi.services.auth.jwt.JwtUtil;
import com.ingsoft.tfi.dto.ApiResponse;
import com.ingsoft.tfi.helpers.JsonParser;
import com.ingsoft.tfi.services.SistemaClinica;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/admin")
public class AdminController {

    private SistemaClinica sistemaClinica;

    public AdminController(SistemaClinica sistemaClinica) {
        this.sistemaClinica = sistemaClinica;
    }

    @PostMapping("/medico")
    public ResponseEntity<JsonNode> agregarMedico(@RequestBody JsonNode jsonMedico, @RequestHeader String token){

        try {

            JwtUtil jwtUtil = new JwtUtil();

            boolean isValid = jwtUtil.isValid(token);

            if (!isValid) {
                // Si el token no es válido, devolver respuesta de error
                ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(),
                        "Token inválido o expirado", null);
                JsonNode jsonResponse = JsonParser.responseAJson(response);
                return new ResponseEntity<>(jsonResponse, HttpStatus.UNAUTHORIZED);
            }
            MedicoModel medico = JsonParser.medicoDesdeJson(jsonMedico);
            String respuesta = sistemaClinica.agregarMedico(medico);

            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.OK.value(),
                    respuesta,
                    JsonParser.medicoAJson(medico));

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al agregar el medico: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}/evolucion/{idEvolucion}")
//    public ResponseEntity<JsonNode> editarEvolucion(@PathVariable String dniPaciente,
//                                                    @PathVariable Long idDiagnostico,
//                                                    @PathVariable Long idEvolucion,
//                                                    @RequestBody JsonNode json){
//        try {
//
//            var paciente = this.sistemaClinica.editarEvolucion(
//                    idEvolucion,
//                    medicoPrueba,
//                    dniPaciente,
//                    idDiagnostico,
//                    JsonParser.informeDesdeJson(json),
//                    JsonParser.recetaDigitalDesdeJson(json),
//                    JsonParser.pedidoLaboratorioDesdeJson(json)
//            );
//
//            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.OK.value(),
//                    "Evolucion editada exitosamente.",
//                    JsonParser.pacienteAJson(paciente));
//
//            JsonNode jsonResponse = JsonParser.responseAJson(response);
//
//            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
//        } catch (Exception e) {
//            ApiResponse<List<JsonNode>> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    "Error al editar evolucion: " + e.getMessage(),
//                    null);
//
//            JsonNode jsonResponse = JsonParser.responseAJson(response);
//
//            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//
//    @DeleteMapping("/paciente/{dniPaciente}/diagnostico/{idDiagnostico}/evolucion/{idEvolucion}")
//    public ResponseEntity<JsonNode> eliminarEvolucion(@PathVariable String dniPaciente,
//                                                      @PathVariable Long idDiagnostico,
//                                                      @PathVariable Long idEvolucion){
//
//        try {
//            this.sistemaClinica.eliminarEvolucion(dniPaciente,
//                    idDiagnostico,
//                    idEvolucion);
//
//            ApiResponse<List<JsonNode>> response = new ApiResponse<>(HttpStatus.CREATED.value(),
//                    "Evolucion eliminada exitosamente.",
//                    null);
//
//            JsonNode jsonResponse = JsonParser.responseAJson(response);
//
//            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
//        } catch (Exception e) {
//            ApiResponse<List<JsonNode>> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    "Error al eliminar evolucion: " + e.getMessage(),
//                    null);
//
//            JsonNode jsonResponse = JsonParser.responseAJson(response);
//
//            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}
