package com.ingsoft.tfi.services.auth.jwt;

import com.ingsoft.tfi.domain.models.auth.UserModel;
import com.ingsoft.tfi.domain.models.auth.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas
    // Usa esta clave para firmar y verificar los tokens
    public String generateToken(UserModel user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY) // Usas la clave secreta con la que firmaste el token
                    .parseClaimsJws(token) // Parseas el token
                    .getBody(); // Retorna las claims
        } catch (JwtException | IllegalArgumentException e) {
            // Si ocurre un error, logueamos y retornamos null
            System.out.println("Error al extraer las claims del token: " + e.getMessage());
            return null; // Retornamos null si el token es inválido o no se puede analizar
        }
    }


    public Authentication getAuthentication(String token) {
        Claims claims = extractClaims(token); // Extraer las claims del JWT
        String username = claims.getSubject(); // Extraer el "sub" que es el nombre de usuario
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims); // Método para extraer roles

        UserPrincipal principal = new UserPrincipal(username); // Suponiendo que el "sub" es el username

        // Crear el token de autenticación con los roles y el usuario
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // Método para extraer roles desde el token (suponiendo que roles están en "roles")
    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        List<String> roles = claims.get("roles", List.class); // Puedes cambiar "roles" por lo que usas en el token
        if (roles == null) {
            return List.of(); // Si no hay roles, retornamos una lista vacía
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Agregar "ROLE_" a cada rol
                .toList();
    }


}
