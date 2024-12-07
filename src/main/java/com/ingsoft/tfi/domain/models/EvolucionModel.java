package com.ingsoft.tfi.domain.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.helpers.JsonParser;
import jakarta.persistence.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity
@Table(name = "evoluciones")
public class EvolucionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id_evolucion;

    @Column
    public Date fecha;
    @Column
    public String informe;

    @ManyToOne
    @JoinColumn(name = "id_diagnostico")
    private DiagnosticoModel diagnostico;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_medico")
    private MedicoModel medico;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_receta")
    private RecetaDigitalModel recetaDigital;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pedido_laboratorio")
    private PedidoLaboratorioModel pedidoLaboratorio;

    public EvolucionModel(String informe, Date fecha, MedicoModel medico,
                          JsonNode recetaDigitalJson, List<MedicamentoModel> medicamentos, Map<String,Integer> medicamentosAmount, String pedidoLaboratorio) {


        if (informe == null || informe.equals("")){
            throw new RuntimeException("Informe vacio o nulo.");
        }
        if (informe.trim().equals("")){
            throw new RuntimeException("Informe vacio o nulo.");
        }

        this.informe = informe;
        this.fecha = fecha;
        this.medico = medico;

        var posibleReceta = Optional.ofNullable(recetaDigitalJson);
        var posiblePedidoLab = Optional.ofNullable(pedidoLaboratorio);

        if(posibleReceta.isPresent() && posiblePedidoLab.isPresent()) {
            throw new IllegalArgumentException("No se puede crear una evolución con receta y con pedido de laboratorio");
        }

        if(posibleReceta.isPresent()) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = formatter.parse(recetaDigitalJson.get("fecha").asText());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String descripcion = recetaDigitalJson.get("descripcion").asText();
            RecetaDigitalModel recetaDigitalObject = new RecetaDigitalModel(
                    date,
                    descripcion,
                    medicamentos,
                    medicamentosAmount
            );
            this.recetaDigital = recetaDigitalObject;
        }

        if(posiblePedidoLab.isPresent()){
            PedidoLaboratorioModel pedidoLaboratorioObject = new PedidoLaboratorioModel(pedidoLaboratorio);
            this.pedidoLaboratorio = pedidoLaboratorioObject;
        }
    }

    public EvolucionModel() {

    }

    public boolean tiene(MedicoModel medico, String informe){
        return this.informe.equals(informe) && this.medico.equals(medico);
    }

    public RecetaDigitalModel getRecetaDigital(){
        return recetaDigital;
    }

    public DiagnosticoModel getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(DiagnosticoModel diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Long getId_evolucion() {
        return id_evolucion;
    }

    public void setId_evolucion(Long id_evolucion) {
        this.id_evolucion = id_evolucion;
    }

    public MedicoModel getMedico() {
        return medico;
    }

    public void setMedico(MedicoModel medico) {
        this.medico = medico;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public PedidoLaboratorioModel getPedidoLaboratorio() {
        return pedidoLaboratorio;
    }

    public void setPedidoLaboratorio(PedidoLaboratorioModel pedidoLaboratorio) {
        this.pedidoLaboratorio = pedidoLaboratorio;
    }

    public void setRecetaDigital(RecetaDigitalModel recetaDigital) {
        this.recetaDigital = recetaDigital;
    }
}
