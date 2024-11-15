package com.ingsoft.tfi.domain.models.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private String username; // El nombre de usuario, por ejemplo, el 'sub' de las claims
    private String password; // Si es necesario, puedes agregar la contraseña
    private Collection<SimpleGrantedAuthority> authorities;

    public UserPrincipal(String username) {
        this.username = username;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null; // No se requiere la contraseña para la autenticación por JWT, pero la puedes incluir si es necesario
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
