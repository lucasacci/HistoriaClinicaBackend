package com.ingsoft.tfi.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Solo admin puede acceder
                        .requestMatchers("/medico/**").hasRole("MEDICO") // Solo médicos pueden acceder
                        .requestMatchers("/recepcionista/**").hasRole("RECEPCIONISTA") // Solo recepcionistas pueden acceder
                        .requestMatchers("/auth/login").permitAll()
                        .anyRequest().authenticated() // El resto de rutas requiere autenticación
                )

                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF si es una API
                .formLogin(form -> form.disable())
                .logout(logout -> logout.permitAll()) // Permite el logout
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
