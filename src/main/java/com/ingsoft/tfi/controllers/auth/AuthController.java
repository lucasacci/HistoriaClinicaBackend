package com.ingsoft.tfi.controllers.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.ingsoft.tfi.dto.ApiResponse;
import com.ingsoft.tfi.dto.LoginRequest;
import com.ingsoft.tfi.dto.TokenResponse;
import com.ingsoft.tfi.helpers.JsonParser;
import com.ingsoft.tfi.services.auth.AuthService;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Entrando en el método login");
        try {
            System.out.println("Login Request: " + loginRequest);
            // Realiza la autenticación y genera el token
            TokenResponse tokenResponse = authService.login(loginRequest);

            // Genera la respuesta exitosa con el token
            ApiResponse<TokenResponse> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Logueo Exitoso!", tokenResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Login Request: " + loginRequest);
            e.printStackTrace();
            // Genera una respuesta de error específica
            ApiResponse<TokenResponse> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Error al loguearse: " + e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<JsonNode> login(@RequestBody final LoginRequest request) {
//        try {
//            TokenResponse tokenResponse = authService.login(request);
//
//            ApiResponse<List<JsonNode>> response = new ApiResponse<>(HttpStatus.OK.value(),
//                    "Logueo Exitoso!",
//                    ResponseEntity.ok(tokenResponse));
//
//            JsonNode jsonResponse = JsonParser.responseAJson(response);
//
//            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
//        } catch (Exception e) {
//            ApiResponse<JsonNode> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    "Error al loguearse" + e.getMessage(),
//                    null);
//
//            JsonNode jsonResponse = JsonParser.responseAJson(response);
//
//            return new ResponseEntity<>(jsonResponse, HttpStatus.BAD_REQUEST);
//        }
//    }

//    @PostMapping("/refresh")
//    public ResponseEntity<JsonNode> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final JsonNode json) {
//
//        String authHeader = JsonParser.authRequestDesdeJson(json);
//
//        return authService.refreshToken(authHeader);
//    }

}
