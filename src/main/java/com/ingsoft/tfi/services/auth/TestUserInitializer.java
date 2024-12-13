package com.ingsoft.tfi.services.auth;

import com.ingsoft.tfi.domain.models.MedicoModel;
import com.ingsoft.tfi.domain.models.UserModel;
import com.ingsoft.tfi.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestUserInitializer implements CommandLineRunner {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Inicializando usuario de prueba...");
        
        if (!userRepository.findByEmail("testuser@example.com").isPresent()) {
            UserModel testUser = new UserModel();
            Date fecha = new Date();
            
            // Crear un médico con todos los datos necesarios
            MedicoModel medico = new MedicoModel(
                null,                    // id_medico (dejar que JPA lo genere)
                "Juan",                  // nombre
                "Pérez",                // apellido
                "12345678",             // dni
                "testuser@example.com", // email
                fecha,                  // fechaNacimiento
                "Calle Principal 123",  // direccion
                "123456789",           // telefono
                "MP12345",             // matricula
                "Cardiólogo"           // especialidad
            );
            
            // Configurar el usuario
            testUser.setEmail("testuser@example.com");
            testUser.setPassword(passwordEncoder.encode("testpassword"));
            
            // Establecer la relación bidireccional
            testUser.setMedico(medico);
            medico.setUser(testUser);
            
            try {
                UserModel savedUser = userRepository.save(testUser);
                System.out.println("Usuario de prueba creado exitosamente:");
                System.out.println("Email: testuser@example.com");
                System.out.println("Password: testpassword");
                System.out.println("Médico ID: " + savedUser.getMedico().getId_medico());
                System.out.println("Médico: " + savedUser.getMedico().getNombre() + " " + savedUser.getMedico().getApellido());
            } catch (Exception e) {
                System.err.println("Error al crear usuario de prueba: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El usuario de prueba ya existe");
        }
    }
}