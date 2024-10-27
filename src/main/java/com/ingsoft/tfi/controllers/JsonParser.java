package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ingsoft.tfi.models.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static PacienteModel pacienteDesdeJson(JsonNode json) {
        // Extraer campos básicos
        String nombre = json.get("nombre").asText();
        String apellido = json.get("apellido").asText();
        String dni = json.get("dni").asText();
        String email = json.get("email").asText();
        String direccion = json.get("direccion").asText();
        int telefono = json.get("telefono").asInt();

        // Formateador de fecha para "yyyy-MM-dd"
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaNacimiento = null;

        try {
            fechaNacimiento = formatoFecha.parse(json.get("fechaNacimiento").asText());
        } catch (ParseException e) {
            e.printStackTrace(); // Manejo de error al parsear la fecha
        }


        // Extraer lista de diagnosticos preexistentes si está presente
        List<String> diagnosticosPreexistentes = new ArrayList<>();
        if (json.has("diagnosticosPreexistentes")) {
            for (JsonNode diagnosticoNode : json.get("diagnosticosPreexistentes")) {
                diagnosticosPreexistentes.add(diagnosticoNode.asText());
            }
        }

        // Crear instancia de PacienteModel
        return new PacienteModel(nombre, apellido, dni, email, fechaNacimiento, direccion, telefono, diagnosticosPreexistentes);
    }


    private static HistoriaClinicaModel historiaClinicaDesdeJson(JsonNode json) {
        HistoriaClinicaModel historiaClinica = new HistoriaClinicaModel();
        List<DiagnosticoModel> diagnosticos = new ArrayList<>();

        json.get("diagnosticos").forEach(diagnosticoJson -> {
            diagnosticos.add(diagnosticoDesdeJson(diagnosticoJson));
        });

        historiaClinica.setDiagnosticos(diagnosticos);
        return historiaClinica;
    }

    private static DiagnosticoModel diagnosticoDesdeJson(JsonNode json) {
        DiagnosticoModel diagnostico = new DiagnosticoModel();

        diagnostico.setDescripcion(json.get("informe").asText());

        List<EvolucionModel> evoluciones = new ArrayList<>();
        json.get("evoluciones").forEach(evolucionJson -> {
            evoluciones.add(evolucionDesdeJson(evolucionJson));
        });
        diagnostico.setEvoluciones(evoluciones);

        return diagnostico;
    }

    private static EvolucionModel evolucionDesdeJson(JsonNode json) {
        EvolucionModel evolucion = new EvolucionModel();

        evolucion.setInforme(json.get("informe").asText());
        evolucion.setMedico(new MedicoModel(json.get("doctor").asText())); // Simplificado para ejemplo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = Instant.from(formatter.parse(json.get("fecha").asText()));
        evolucion.setFecha(Date.from(instant));

        return evolucion;
    }



}
