package com.mzuha.taskmanager.controller;

import com.mzuha.taskmanager.model.AuthDto;
import com.mzuha.taskmanager.model.AuthTokenDto;
import com.mzuha.taskmanager.model.RoleEntity;
import com.mzuha.taskmanager.model.UserEntity;
import com.mzuha.taskmanager.repository.RoleRepository;
import com.mzuha.taskmanager.repository.UserRepository;
import com.mzuha.taskmanager.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil tokenGenerator;

    public AuthenticationController(UserRepository userRepository,
                                    AuthenticationManager authenticationManager,
                                    RoleRepository roleRepository,
                                    PasswordEncoder passwordEncoder,
                                    JwtUtil tokenGenerator) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDto authDto) {
        if (userRepository.findByUsername(authDto.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username already taken!", HttpStatus.BAD_REQUEST);
        }

        RoleEntity userRole = roleRepository.findByName("USER").orElseThrow(
            () -> new RuntimeException("Role not found!")
        );
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(authDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(authDto.getPassword()));
        userEntity.setRoles(List.of(userRole));

        userRepository.save(userEntity);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> login(@RequestBody AuthDto authDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authDto.getUsername(),
                authDto.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenGenerator.generateJWTToken(authentication);
        return new ResponseEntity<>(new AuthTokenDto(token), HttpStatus.OK);
    }
}
