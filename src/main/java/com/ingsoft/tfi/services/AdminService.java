package com.ingsoft.tfi.services;

import com.ingsoft.tfi.domain.models.MedicoModel;
import com.ingsoft.tfi.domain.models.auth.UserModel;
import com.ingsoft.tfi.repositories.IMedicoRepository;
import com.ingsoft.tfi.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdminService {
    @Autowired
    private IUserRepository userRepository;
    private IMedicoRepository medicoRepository;

//    public UserModel crearMedico(String email,
//                                 String password,
//                                 String nombre,
//                                 String apellido,
//                                 String dni,
//                                 Date fechaNacimiento,
//                                 String direccion,
//                                 String telefono,
//                                 String matricula,
//                                 String especialidad) {
//        UserModel medico = new UserModel();
//        medico.setUsername(email);
//        medico.setPassword(password);
//        medico.setRole(UserModel.Role.MEDICO);
//        medicoRepository.save(new MedicoModel());
//        return userRepository.save(medico);
//    }

//    public UserModel crearRecepcionista(String email, String password) {
//        UserModel recepcionista = new UserModel();
//        recepcionista.setUsername(email);
//        recepcionista.setPassword(password);
//        recepcionista.setRole(UserModel.Role.RECEPCIONISTA);
//        return userRepository.save(recepcionista);
//    }
}
