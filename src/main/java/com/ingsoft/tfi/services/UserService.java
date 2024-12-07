package com.ingsoft.tfi.services;

import com.ingsoft.tfi.domain.models.UserModel;
import com.ingsoft.tfi.repositories.IUserRepository;
import com.ingsoft.tfi.domain.models.MedicoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void init() {
        createTestUser();
    }

    public void createTestUser() {
        System.out.println("Verificando si existe usuario de prueba...");
        Optional<UserModel> existingUser = userRepository.findByEmail("testuser@example.com");
        
        if (existingUser.isPresent()) {
            System.out.println("Usuario de prueba encontrado, actualizando datos del médico...");
            UserModel user = existingUser.get();
            
            // Crear un nuevo médico si no existe
            if (user.getMedico() == null) {
                Date fecha = new Date();
                MedicoModel medico = new MedicoModel(
                    null,                    // id_medico (dejar que JPA lo genere)
                    "Juan",                 // nombre
                    "Pérez",                // apellido
                    "12345678",             // dni
                    "testuser@example.com", // email
                    fecha,                  // fechaNacimiento
                    "Calle Principal 123",  // direccion
                    123456789,             // telefono
                    "MP12345",             // matricula
                    "Cardiólogo"           // especialidad
                );
                
                // Establecer la relación bidireccional
                user.setMedico(medico);
                medico.setUser(user);
                
                try {
                    UserModel savedUser = userRepository.save(user);
                    System.out.println("Usuario actualizado con médico:");
                    System.out.println("- ID Usuario: " + savedUser.getId());
                    System.out.println("- Email: " + savedUser.getEmail());
                    System.out.println("- Médico: " + savedUser.getMedico().getNombre() + " " + savedUser.getMedico().getApellido());
                    System.out.println("- ID Médico: " + savedUser.getMedico().getId_medico());
                } catch (Exception e) {
                    System.err.println("Error al actualizar usuario: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Creando nuevo usuario de prueba...");
            UserModel testUser = new UserModel();
            Date fecha = new Date();
            
            MedicoModel medico = new MedicoModel(
                null,                    // id_medico
                "Juan",                 // nombre
                "Pérez",                // apellido
                "12345678",             // dni
                "testuser@example.com", // email
                fecha,                  // fechaNacimiento
                "Calle Principal 123",  // direccion
                123456789,             // telefono
                "MP12345",             // matricula
                "Cardiólogo"           // especialidad
            );
            
            testUser.setEmail("testuser@example.com");
            testUser.setPassword(passwordEncoder.encode("testpassword"));
            
            // Establecer la relación bidireccional
            testUser.setMedico(medico);
            medico.setUser(testUser);
            
            try {
                UserModel savedUser = userRepository.save(testUser);
                System.out.println("Nuevo usuario creado con médico:");
                System.out.println("- ID Usuario: " + savedUser.getId());
                System.out.println("- Email: " + savedUser.getEmail());
                System.out.println("- Médico: " + savedUser.getMedico().getNombre() + " " + savedUser.getMedico().getApellido());
                System.out.println("- ID Médico: " + savedUser.getMedico().getId_medico());
            } catch (Exception e) {
                System.err.println("Error al crear usuario: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean validateUser(String email, String password) {
        System.out.println("Validando usuario con email: " + email);
        try {
            Optional<UserModel> user = userRepository.findByEmail(email);
            System.out.println("Usuario encontrado: " + user.isPresent());
            
            if (user.isPresent()) {
                System.out.println("Verificando contraseña para usuario: " + user.get().getEmail());
                System.out.println("Tiene médico asociado: " + (user.get().getMedico() != null));
                
                boolean matches = passwordEncoder.matches(password, user.get().getPassword());
                System.out.println("Contraseña válida: " + matches);
                
                return matches;
            }
        } catch (Exception e) {
            System.err.println("Error validando usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
