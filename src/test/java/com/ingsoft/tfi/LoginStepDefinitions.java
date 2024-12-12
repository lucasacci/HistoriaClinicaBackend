package com.ingsoft.tfi;

import com.ingsoft.tfi.domain.models.MedicoModel;
import com.ingsoft.tfi.domain.models.UserModel;
import com.ingsoft.tfi.repositories.IUserRepository;
import com.ingsoft.tfi.services.UserService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginStepDefinitions {

    private IUserRepository userRepository;
    private UserService userService = new UserService();
    private UserModel user = new UserModel();
    private Boolean isValidUser;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String mailMedicoLlamada;
    private String passwdMedicoLlamada;
    private String apellidoMedicoLlamada;

    @Given("Existe el medico {string} registrado con mail {string} y contrasenia {string}")
        public void existeElMedicoRegistradoConMailYContrasenia(String arg0, String arg1, String arg2) {
        mailMedicoLlamada = arg1;
        passwdMedicoLlamada = arg2;
        apellidoMedicoLlamada = arg0;
        Date fecha = new Date();
        String email = "testuser@example.com";
        String passwd = "testpassword";
        MedicoModel medico = new MedicoModel(
                null,                    // id_medico
                "Juan",                 // nombre
                "Perez",                // apellido
                "12345678",             // dni
                email, // email
                fecha,                  // fechaNacimiento
                "Calle Principal 123",  // direccion
                "123456789",             // telefono
                "MP12345",             // matricula
                "Cardiólogo"           // especialidad
        );
        this.user.setMedico(medico);
        this.user.setEmail(email);
        this.user.setPassword(passwordEncoder.encode(passwd));

        this.userRepository = mock(IUserRepository.class);
        when(this.userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        this.userService.setUserRepository(userRepository);
    }

    @When("Inicio sesion")
    public void inicioSesion() {
        this.isValidUser = userService.validateUser(mailMedicoLlamada, passwdMedicoLlamada);
    }

    @Then("Devuelve mensaje de inicio de sesion exitoso")
    public void devuelveMensajeDeInicioDeSesionExitoso() {
        assertThat(isValidUser).isTrue();
        assertThat(user.getMedico().getApellido()).isEqualTo(apellidoMedicoLlamada);
        System.out.println("Sesion iniciada con exito");
    }

    @Then("Devuelve mensaje de Error de Autenticacion")
    public void devuelveMensajeDeErrorDeAutenticacion() {
        assertThat(isValidUser).isFalse();
        System.out.println("Error de autenticacion");
    }

    @Given("El medico {string} con mail {string} y contrasenia {string}")
    public void elMedicoConMailYContrasenia(String arg0, String arg1, String arg2) {
        mailMedicoLlamada = arg1;
        passwdMedicoLlamada = arg2;
        apellidoMedicoLlamada = arg0;
        Date fecha = new Date();
        String email = "testuser@example.com";
        String passwd = "testpassword";
        MedicoModel medico = new MedicoModel(
                null,                    // id_medico
                "Juan",                 // nombre
                "Perez",                // apellido
                "12345678",             // dni
                email, // email
                fecha,                  // fechaNacimiento
                "Calle Principal 123",  // direccion
                "123456789",             // telefono
                "MP12345",             // matricula
                "Cardiólogo"           // especialidad
        );
        this.user.setMedico(medico);
        this.user.setEmail(email);
        this.user.setPassword(passwordEncoder.encode(passwd));

        this.userRepository = mock(IUserRepository.class);
        when(this.userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        this.userService.setUserRepository(userRepository);
    }

    @Then("Devuelve mensaje de medico no registrado")
    public void devuelveMensajeDeMedicoNoRegistrado() {
        assertThat(user.getMedico().getApellido()).isNotEqualTo(apellidoMedicoLlamada);
        System.out.println("Medico no registrado");
    }
}
