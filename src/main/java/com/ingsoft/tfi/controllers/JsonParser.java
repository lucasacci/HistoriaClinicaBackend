package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ingsoft.tfi.models.*;
import jdk.jshell.Diag;
import org.springframework.cache.support.NullValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class JsonParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String informeDesdeJson(JsonNode json){
        return json.get("informe").asText("");
    }

    public static Optional<RecetaDigitalModel> recetaDigitalDesdeJson(JsonNode json){
        if (!json.has("receta")) {
            return Optional.empty();
        }

        JsonNode jsonReceta = json.get("receta");

        List<RecetaDigitalDetalleModel> recetaDigitalDetalles = new ArrayList<>();

        //TODO: Armar esta funcion para que obtenga los datos de los medicamentos se busquen en la base de datos

        RecetaDigitalModel receteDigitalCabecera = new RecetaDigitalModel();

        jsonReceta.get("medicamentos").forEach( medicamentoJson -> {
            MedicamentoModel medicamento = new MedicamentoModel(
                    medicamentoJson.get("nombreComercial").asText(),
                    medicamentoJson.get("nombreGenerico").asText()
            );

            RecetaDigitalDetalleModel detalle = new RecetaDigitalDetalleModel(
                    medicamentoJson.get("cantidad").asInt(),
                    medicamento,
                    receteDigitalCabecera
            );

            recetaDigitalDetalles.add(detalle);
        });

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(jsonReceta.get("fecha").asText());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        /*RecetaDigitalModel recetaDigital = new RecetaDigitalModel(
                date,
                jsonReceta.get("descripcion").asText(),
                recetaDigitalDetalles
        );*/

        receteDigitalCabecera.setFecha(date);
        receteDigitalCabecera.setDescripcion(jsonReceta.get("descripcion").asText());
        receteDigitalCabecera.setRecetaDigitaldetalle(recetaDigitalDetalles);

        return Optional.of(receteDigitalCabecera);
    }

    public static Optional<PedidoLaboratorioModel> pedidoLaboratorioDesdeJson(JsonNode json){
        if (!json.has("pedidoLaboratorio")) {
            return Optional.empty();
        }

        JsonNode jsonPedidoLab = json.get("pedidoLaboratorio");

        PedidoLaboratorioModel pedidoLaboratorio = new PedidoLaboratorioModel(
                jsonPedidoLab.get("descripcion").asText()
        );

        return Optional.of(pedidoLaboratorio);
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

        diagnostico.getEvoluciones().forEach(evolucion -> {
            array.add(evolucionAJson(evolucion));
        });

        json.put("id_diagnostico", diagnostico.getId());
        json.put("descripcion", diagnostico.getDescripcion());
        json.set("evoluciones", array);
        return json;
    }

    public static JsonNode evolucionAJson(EvolucionModel evolucion){
        ObjectNode json = mapper.createObjectNode();

        Instant instant = evolucion.getFecha().toInstant(); // date a instant
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        String formattedDate = formatter.format(instant);

        json.put("id_evolucion", evolucion.getId_evolucion());
        json.put("informe", evolucion.getInforme());
        json.put("doctor", evolucion.getMedico().getNombre());
        json.put("fecha", formattedDate);

        if (evolucion.getRecetaDigital() != null) {
            json.set("receta", recetaDigitalAJson(evolucion.getRecetaDigital()));
        }

        return json;
    }

    private static JsonNode recetaDigitalAJson(RecetaDigitalModel recetaDigital) {
        ObjectNode json = mapper.createObjectNode();
        ArrayNode array = mapper.createArrayNode();

        json.put("fecha", recetaDigital.getFecha().toString());
        json.put("descripcion", recetaDigital.getDescripcion());

        recetaDigital.getRecetaDigitaldetalle().forEach(receta -> {
            array.add(MedicamentoAJson(receta));
        });

        json.set("medicamentos", array);

        return json;
    }

    private static JsonNode MedicamentoAJson(RecetaDigitalDetalleModel recetaDetalle) {
        ObjectNode json = mapper.createObjectNode();

        json.put("nombreComercial", recetaDetalle.getMedicamento().getNombreComercial());
        json.put("nombreGenerico", recetaDetalle.getMedicamento().getNombreGenerico());
        json.put("cantidad", recetaDetalle.getCantidad());

        return json;
    }

    public static PacienteModel pacienteDesdeJson(JsonNode json) {

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


        PacienteModel paciente = new PacienteModel();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setDni(dni);
        paciente.setEmail(email);
        paciente.setTelefono(telefono);
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setDireccion(direccion);

        HistoriaClinicaModel historiaClinica = historiaClinicaDesdeJson(json);
        paciente.setHistoriaClinica(historiaClinica);

        if (historiaClinica != null) {
            historiaClinica.setPaciente(paciente);
        }

        return paciente;

    }


    private static HistoriaClinicaModel historiaClinicaDesdeJson(JsonNode json) {
        HistoriaClinicaModel historiaClinica = new HistoriaClinicaModel();
        List<DiagnosticoModel> diagnosticos = new ArrayList<>();

        if (json.has("historiaClinica") && json.get("historiaClinica").has("diagnosticos")) {
            json.get("historiaClinica").get("diagnosticos").forEach(diagnosticoJson -> {
                DiagnosticoModel diagnostico = new DiagnosticoModel();
                diagnostico.setDescripcion(diagnosticoJson.get("descripcion").asText());
                diagnostico.setHistoriaClinica(historiaClinica);
                diagnosticos.add(diagnostico);
            });
        }

        historiaClinica.setDiagnosticos(diagnosticos);
        return historiaClinica;
    }

    public static String diagnosticoDesdeJson(JsonNode json) {
        return json.get("descripcion").asText("");
    }





}
