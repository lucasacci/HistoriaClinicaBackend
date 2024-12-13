package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.domain.models.PacienteModel;
import com.ingsoft.tfi.domain.models.UserModel;
import com.ingsoft.tfi.dto.ApiResponse;
import com.ingsoft.tfi.dto.LoginRequest;
import com.ingsoft.tfi.helpers.JsonParser;
import com.ingsoft.tfi.services.AuthService;
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
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JsonNode> login(@RequestBody LoginRequest loginRequest, 
                                        HttpSession session,
                                        HttpServletRequest request) {
        try {
            Map<String, Object> authData = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            
            ApiResponse<JsonNode> response = new ApiResponse<>(
                "Inicio de sesión exitoso!",
                JsonParser.objectToJsonNode(authData)
            );

            JsonNode jsonResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        } catch (AuthenticationException e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(
                "Error de autenticación: " + e.getMessage(),
                null
            );

            JsonNode jsonResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(jsonResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<JsonNode> response = new ApiResponse<>(
                "Error interno: " + e.getMessage(),
                null
            );

            JsonNode jsonResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonNode> logout(HttpSession session, 
                                         @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            authService.logout(session, token);
            
            ApiResponse<JsonNode> response = new ApiResponse<>(
                "Sesión cerrada exitosamente",
                null
            );

            JsonNode jsonResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(
                "Error al cerrar sesión: " + e.getMessage(),
                null
            );

            JsonNode jsonResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método de prueba para verificar la autenticación
    @GetMapping("/check")
    public ResponseEntity<String> checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Usuario autenticado como: " + auth.getName() + 
                               " con autoridades: " + auth.getAuthorities());
    }

}
