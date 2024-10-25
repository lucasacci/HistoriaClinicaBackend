package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ingsoft.tfi.models.DiagnosticoModel;
import com.ingsoft.tfi.models.EvolucionModel;
import com.ingsoft.tfi.models.HistoriaClinicaModel;
import com.ingsoft.tfi.models.PacienteModel;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class JsonParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String informeDesdeJson(JsonNode json){
        return json.get("informe").asText("");
    }

    public static JsonNode pacienteAJson(PacienteModel paciente){
        ObjectNode json= mapper.createObjectNode();

        json.put("nombre" , paciente.getNombre());
        json.put("apellido" , paciente.getApellido());
        json.put("dni" , paciente.getDni());
        json.put("email" , paciente.getEmail());
        json.put("telefono" , paciente.getTelefono());
        json.set("historiaClinica", historiaClinicaAJson(paciente.getHistoriaClinica()));

        return json;
    }

    public static JsonNode historiaClinicaAJson(HistoriaClinicaModel historiaClinica){
        ObjectNode json = mapper.createObjectNode();
        ArrayNode array = mapper.createArrayNode();

        historiaClinica.getDiagnosticos().forEach(diagnostico -> array.add(diagnosticoAJson(diagnostico)));

        json.set("diagnosticos", array);

        return json;
    }

    public static JsonNode diagnosticoAJson(DiagnosticoModel diagnostico){
        ObjectNode json = mapper.createObjectNode();
        ArrayNode array = mapper.createArrayNode();

        diagnostico.getEvoluciones().forEach(evolucion -> array.add(evolucionAJson(evolucion)));
        json.put("id_diagnostico", diagnostico.getId());
        json.put("nombre", diagnostico.getDescripcion());
        json.set("evoluciones", array);
        return json;
    }

    public static JsonNode evolucionAJson(EvolucionModel evolucion){
        ObjectNode json = mapper.createObjectNode();

        Instant instant = evolucion.getFecha().toInstant(); // date a instant
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        String formattedDate = formatter.format(instant);

        json.put("informe", evolucion.getInforme());
        json.put("doctor", evolucion.getMedico().getNombre());
        json.put("fecha", formattedDate);
        return json;
    }
}
