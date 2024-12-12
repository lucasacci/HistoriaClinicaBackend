package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.domain.models.PacienteModel;
import com.ingsoft.tfi.domain.models.UserModel;
import com.ingsoft.tfi.dto.ApiResponse;
import com.ingsoft.tfi.dto.LoginRequest;
import com.ingsoft.tfi.helpers.JsonParser;
import com.ingsoft.tfi.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import javax.naming.AuthenticationException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JsonNode> login(@RequestBody LoginRequest loginRequest, 
                                        HttpSession session,
                                        HttpServletRequest request) {
        try {
            System.out.println("Intento de login para email: " + loginRequest.getEmail());
            
            // Verificar credenciales
            boolean isValidUser = userService.validateUser(loginRequest.getEmail(), loginRequest.getPassword());
            System.out.println("Credenciales válidas: " + isValidUser);

            if (!isValidUser) {
                throw new AuthenticationException("Usuario no encontrado o contraseña incorrecta");
            }

            // Verificar que el usuario tenga un médico asociado
            var userOptional = userService.findByEmail(loginRequest.getEmail());
            System.out.println("Usuario encontrado en BD: " + userOptional.isPresent());
            
            if (userOptional.isPresent()) {
                UserModel user = userOptional.get();
                System.out.println("ID del usuario: " + user.getId());
                System.out.println("Email del usuario: " + user.getEmail());
                System.out.println("Médico asociado: " + (user.getMedico() != null));
                
                if (user.getMedico() != null) {
                    System.out.println("Datos del médico:");
                    System.out.println("- ID: " + user.getMedico().getId_medico());
                    System.out.println("- Nombre: " + user.getMedico().getNombre());
                    System.out.println("- Apellido: " + user.getMedico().getApellido());
                    System.out.println("- Matrícula: " + user.getMedico().getMatricula());
                }
            }

            if (userOptional.isEmpty() || userOptional.get().getMedico() == null) {
                throw new AuthenticationException("El usuario no tiene un médico asociado");
            }

            // Crear una lista de autoridades
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
            );

            // Crear el token de autenticación
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), 
                        null,
                        authorities
                    );

            // Establecer detalles adicionales
            authToken.setDetails(new WebAuthenticationDetails(request));

            // Establecer la autenticación en el contexto de seguridad
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authToken);
            SecurityContextHolder.setContext(securityContext);
            
            // Guardar el contexto de seguridad en la sesión
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
            
            // Guardar información adicional en la sesión
            session.setAttribute("USER_EMAIL", loginRequest.getEmail());
            session.setAttribute("MEDICO", userOptional.get().getMedico());

            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Inicio de sesión exitoso!",
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Error de autenticación: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(jsonResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();  // Agregado para ver el stack trace completo
            ApiResponse<JsonNode> response = new ApiResponse<>(
                    "Error interno: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonNode> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual
        ApiResponse<JsonNode> response = new ApiResponse<>(
                "Sesión cerrada exitosamente.",
                null);

        JsonNode jsonResponse = JsonParser.responseAJson(response);
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    // Método de prueba para verificar la autenticación
    @GetMapping("/check")
    public ResponseEntity<String> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Usuario autenticado como: " + auth.getName() + 
                               " con autoridades: " + auth.getAuthorities());
    }

}
