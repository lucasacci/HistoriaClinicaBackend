package com.ingsoft.tfi.services;

import com.ingsoft.tfi.domain.models.EvolucionModel;
import com.ingsoft.tfi.domain.models.MedicoModel;
import com.ingsoft.tfi.domain.models.PacienteModel;
import com.ingsoft.tfi.repositories.IRepositorioPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class SistemaClinicaTest {

    private SistemaClinica sistemaClinica;
    private PacienteService pacienteService;
    private IRepositorioPaciente repositorioPaciente;

    @BeforeEach
    public void setUp(){
        this.repositorioPaciente = mock(IRepositorioPaciente.class);
        this.pacienteService = new PacienteService(repositorioPaciente);
        this.sistemaClinica = new SistemaClinica(pacienteService);
    }

    @Test
    public void agregarEvolucionDeTextoSimpleExitosamente(){
        //Arrange
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = sdf.parse("23/02/2000");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        MedicoModel medico = new MedicoModel(1l,"Lucho",
                "Casacci","x","colombia",fecha,"m2a",
                "38123","232","pediatra");

        String dniPaciente = "123456";
        Long idDiagnostico = 1L;
        String informeSimple = "El paciente tiene mucha toz";
        List<String> listaDiagnosticos = new ArrayList<>();
        listaDiagnosticos.add("sida");
        PacienteModel paciente = new PacienteModel("nico", "fuentes", dniPaciente, "mail@mail.com",
                fecha, "avenida siempremuerta", "123456", listaDiagnosticos);
        paciente.getHistoriaClinica().getDiagnosticos().getFirst().setId(idDiagnostico);
        when(this.repositorioPaciente.findByDni(dniPaciente)).thenReturn(Optional.of(paciente));

        //Act
        PacienteModel pacienteRespuesta = sistemaClinica.agregarEvolucion(medico, dniPaciente, idDiagnostico, informeSimple,null, null, null, null);

        //Assert
        assertThat(pacienteRespuesta.getHistoriaClinica().getDiagnosticos().getFirst().getEvoluciones().getFirst().getInforme()).isEqualTo(informeSimple);
    }


    @Test
    public void agregarEvolucionDeTextoSimpleVaciaDevuelveError(){
        //Arrange
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = sdf.parse("23/02/2000");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        MedicoModel medico = new MedicoModel(1l,"Lucho",
                "Casacci","x","colombia",fecha,"m2a",
                "38123","232","pediatra");
        String dniPaciente = "123456";
        Long idDiagnostico = 1L;
        String informeSimple = "";
        List<String> listaDiagnosticos = new ArrayList<>();
        listaDiagnosticos.add("sida");
        PacienteModel paciente = new PacienteModel("nico", "fuentes", dniPaciente, "mail@mail.com",
                fecha, "avenida siempremuerta", "123456", listaDiagnosticos);
        paciente.getHistoriaClinica().getDiagnosticos().getFirst().setId(idDiagnostico);
        when(this.repositorioPaciente.findByDni(dniPaciente)).thenReturn(Optional.of(paciente));

        //Act
        assertThatThrownBy(() -> sistemaClinica.agregarEvolucion(medico, dniPaciente, idDiagnostico, informeSimple,null, null, null, null))
                .hasMessage("Informe vacio o nulo.");

        //Assert
    }

    @Test
    public void agregarEvolucionDeTextoSimpleNulaDevuelveError(){
        //Arrange
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = sdf.parse("23/02/2000");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        MedicoModel medico = new MedicoModel(1l,"Lucho",
                "Casacci","x","colombia",fecha,"m2a",
                "38123","232","pediatra");
        String dniPaciente = "123456";
        Long idDiagnostico = 1L;
        String informeSimple = null;
        List<String> listaDiagnosticos = new ArrayList<>();
        listaDiagnosticos.add("sida");
        PacienteModel paciente = new PacienteModel("nico", "fuentes", dniPaciente, "mail@mail.com",
                fecha, "avenida siempremuerta", "123456", listaDiagnosticos);
        paciente.getHistoriaClinica().getDiagnosticos().getFirst().setId(idDiagnostico);
        when(this.repositorioPaciente.findByDni(dniPaciente)).thenReturn(Optional.of(paciente));

        //Act
        assertThatThrownBy(() -> sistemaClinica.agregarEvolucion(medico, dniPaciente, idDiagnostico, informeSimple,null, null, null, null))
                .hasMessage("Informe vacio o nulo.");

        //Assert
    }

    @Test
    public void agregarEvolucionDeTextoSimpleSoloEspaciosDevuelveError(){
        //Arrange
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;
        try {
            fecha = sdf.parse("23/02/2000");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        MedicoModel medico = new MedicoModel(1l,"Lucho",
                "Casacci","x","colombia",fecha,"m2a",
                "38123","232","pediatra");
        String dniPaciente = "123456";
        Long idDiagnostico = 1L;
        String informeSimple = "                          ";
        List<String> listaDiagnosticos = new ArrayList<>();
        listaDiagnosticos.add("sida");
        PacienteModel paciente = new PacienteModel("nico", "fuentes", dniPaciente, "mail@mail.com",
                fecha, "avenida siempremuerta", "123456", listaDiagnosticos);
        paciente.getHistoriaClinica().getDiagnosticos().getFirst().setId(idDiagnostico);
        when(this.repositorioPaciente.findByDni(dniPaciente)).thenReturn(Optional.of(paciente));

        //Act
        assertThatThrownBy(() -> sistemaClinica.agregarEvolucion(medico, dniPaciente, idDiagnostico, informeSimple,null, null, null, null))
                .hasMessage("Informe vacio o nulo.");

        //Assert
    }
}