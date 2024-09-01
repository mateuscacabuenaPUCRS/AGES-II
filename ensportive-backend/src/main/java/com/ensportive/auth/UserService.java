package com.ensportive.auth;

import com.ensportive.auth.dtos.RegisterDTO;
import com.ensportive.students.StudentEntity;
import com.ensportive.students.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Long createStudentUser(RegisterDTO registerDTO){
        if(registerDTO.getUsername() == null){
            throw new IllegalArgumentException("Username is required");
        }
        if(registerDTO.getPassword() == null){
            throw new IllegalArgumentException("Password is required");
        }
        if(registerDTO.getStudentId() == null){
            throw new IllegalArgumentException("Student is required");
        }
        if (userRepository.existsByStudentId(registerDTO.getStudentId())) {
            throw new IllegalArgumentException("User is already created");
        } else if (userRepository.existsByUsername(registerDTO.getUsername())){
            throw new IllegalArgumentException("Username is already in use.");
        }
        StudentEntity studentEntity = studentRepository.findById(registerDTO.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found by id"));

        UserEntity user = new UserEntity();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setStudent(studentEntity);
        RoleEntity role = roleRepository.findByName("STUDENT").get();
        Set<RoleEntity> roles = Collections.singleton(role);
        user.setRoles(roles);
        userRepository.save(user);

        studentEntity.setUser(user);
        studentRepository.save(studentEntity);

        return registerDTO.getStudentId();
    }

    public UserEntity createAdminUser(RegisterDTO registerDTO){
        if(registerDTO.getUsername() == null){
            throw new IllegalArgumentException("Username is required");
        }
        if(registerDTO.getPassword() == null){
            throw new IllegalArgumentException("Password is required");
        }
        if (userRepository.existsByUsername(registerDTO.getUsername())){
            throw new IllegalArgumentException("Username is already in use.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        RoleEntity role = roleRepository.findByName("ADMIN").get();
        Set<RoleEntity> roles = Collections.singleton(role);
        user.setRoles(roles);
        userRepository.save(user);

        return user;
    }

    public UserEntity update(Long id, RegisterDTO registerDTO){
        if(registerDTO.getUsername() == null){
            throw new IllegalArgumentException("Username is required");
        }
        if(registerDTO.getPassword() == null){
            throw new IllegalArgumentException("Password is required");
        }

        UserEntity user =
                userRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            UserEntity userByUsername = userRepository.findByUsername(registerDTO.getUsername()).orElse(null);
            if (user.getId() != userByUsername.getId()) {
                throw new IllegalArgumentException("Username is already in use.");
            }
        }

        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userRepository.save(user);

        return user;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
