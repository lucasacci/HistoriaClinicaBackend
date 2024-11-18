package com.ingsoft.tfi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.domain.models.PacienteModel;
import com.ingsoft.tfi.dto.ApiResponse;
import com.ingsoft.tfi.dto.LoginRequest;
import com.ingsoft.tfi.helpers.JsonParser;
import com.ingsoft.tfi.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JsonNode> login(@RequestBody LoginRequest loginRequest) {
        try{

            System.out.println("email: " + loginRequest.getEmail());

            boolean isValidUser = userService.validateUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (!isValidUser) {
                throw new AuthenticationException("Usuario no encontrado o contraseña incorrecta");
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), null, null);
            SecurityContextHolder.getContext().setAuthentication(authToken);

            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Inicio de sesión exitoso!",
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);

            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(),
                    "Error de autenticación: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(jsonResponse, HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error interno: " + e.getMessage(),
                    null);

            JsonNode jsonResponse = JsonParser.responseAJson(response);
            return new ResponseEntity<>(jsonResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonNode> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual
        ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.OK.value(),
                "Sesión cerrada exitosamente.",
                null);

        JsonNode jsonResponse = JsonParser.responseAJson(response);
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }



}
