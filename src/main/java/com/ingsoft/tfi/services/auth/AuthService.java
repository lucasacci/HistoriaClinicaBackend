package com.ingsoft.tfi.services.auth;

import com.ingsoft.tfi.domain.auth.Token;
import com.ingsoft.tfi.domain.models.auth.UserModel;
import com.ingsoft.tfi.dto.LoginRequest;
import com.ingsoft.tfi.dto.TokenResponse;
import com.ingsoft.tfi.repositories.ITokenRepository;
import com.ingsoft.tfi.repositories.IUserRepository;
import com.ingsoft.tfi.services.auth.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final ITokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(IUserRepository userRepository, ITokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponse login(LoginRequest request) {
        // 1. Buscar el usuario en la base de datos
        UserModel user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Verificar la contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // 3. Generar el token JWT
        String token = jwtUtil.generateToken(user);

        // 4. Guardar el token en la base de datos si es necesario (opcional)
        tokenRepository.save(new Token(token, user));

        // 5. Devolver el token en la respuesta
        return new TokenResponse(token);
    }

}
