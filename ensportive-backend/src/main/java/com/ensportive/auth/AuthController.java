package com.ensportive.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ensportive.auth.dtos.LoginDTO;
import com.ensportive.auth.dtos.RegisterDTO;
import com.ensportive.auth.dtos.TokenResponseDTO;
import com.ensportive.configs.security.JwtService;
import com.ensportive.students.dtos.StudentRequestDTO;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()));
        UserEntity user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow();
        TokenResponseDTO tokenResponseDTO = TokenResponseDTO.builder()
                .accessToken(jwtService.generateToken(user.getUsername()))
                .refreshToken(jwtService.generateRefreshToken(user.getUsername()))
                .build();
        return new ResponseEntity<>(tokenResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Long> register(@RequestBody RegisterDTO registerDTO) {
        return new ResponseEntity<>(userService.createStudentUser(registerDTO), HttpStatus.CREATED);
    }

    @GetMapping("/auth/user")
    public ResponseEntity<UserEntity> getUserByName(@RequestParam String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("auth/{id}")
    public ResponseEntity<UserEntity> update(@Parameter(description = "User Id") @PathVariable Long id,
                    @RequestBody RegisterDTO request) {
            return ResponseEntity.ok(userService.update(id, request));
    }

    @PostMapping("/auth/admin/register")
    public ResponseEntity<UserEntity> registerAdmin(@RequestBody RegisterDTO registerDTO) {
        return new ResponseEntity<>(userService.createAdminUser(registerDTO), HttpStatus.CREATED);
    }
 
    @GetMapping("/auth/admins")
    public ResponseEntity<List<UserEntity>> getAdminUsers() {
        RoleEntity role = roleRepository.findByName("ADMIN").get();
        return ResponseEntity.ok().body(userRepository.findByRoles(role));
    }

    @DeleteMapping("auth/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
