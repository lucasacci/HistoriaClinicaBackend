package com.ingsoft.tfi.domain.models.auth;

import com.ingsoft.tfi.domain.auth.Token;
import com.ingsoft.tfi.domain.base.PersonaModel;
import com.ingsoft.tfi.domain.models.MedicoModel;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN, MEDICO, RECEPCIONISTA
    }

    @OneToOne(mappedBy = "user")
    private MedicoModel medico;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}