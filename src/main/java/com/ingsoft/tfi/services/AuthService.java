package com.ingsoft.tfi.services;
import jakarta.servlet.http.HttpSession;
import com.ingsoft.tfi.domain.models.UserModel;
import com.ingsoft.tfi.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> login(String email, String password) throws AuthenticationException {
        // Verificar credenciales
        boolean isValidUser = userService.validateUser(email, password);

        if (!isValidUser) {
            throw new AuthenticationException("Usuario no encontrado o contraseña incorrecta");
        }

        // Obtener usuario y generar token
        var userOptional = userService.findByEmail(email);
        
        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            
            // Crear claims para el token
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", user.getEmail());
            
            if (user.getMedico() != null) {
                claims.put("medicoId", user.getMedico().getId_medico());
                claims.put("medicoNombre", user.getMedico().getNombre());
                claims.put("medicoApellido", user.getMedico().getApellido());
                claims.put("medicoMatricula", user.getMedico().getMatricula());
            }

            // Generar token
            String token = jwtUtil.generateToken(user.getEmail(), claims);

            // Crear respuesta con token y datos del usuario
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("user", Map.of(
                "email", user.getEmail(),
                "medico", user.getMedico() != null ? Map.of(
                    "id", user.getMedico().getId_medico(),
                    "nombre", user.getMedico().getNombre(),
                    "apellido", user.getMedico().getApellido(),
                    "matricula", user.getMedico().getMatricula(),
                    "especialidad", user.getMedico().getEspecialidad()
                ) : null
            ));

            return responseData;
        }

        throw new AuthenticationException("Usuario no encontrado");
    }

    public void logout(HttpSession session, String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            jwtUtil.invalidateToken(token);
        }
        
        SecurityContextHolder.clearContext();
        
        if (session != null) {
            session.invalidate();
        }
    }
} 