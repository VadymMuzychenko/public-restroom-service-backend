package com.example.demo.controllers;

import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.core.role.RoleNames;
import com.example.demo.core.role.RoleRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.security.model.LoginRequest;
import com.example.demo.security.model.RegisterRequest;
import com.example.demo.security.model.TokenResponse;
import com.example.demo.core.user.model.AppUser;
import com.example.demo.core.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (appUserRepository.existsByUsername((registerRequest.getUsername()))) {
            throw new ConflictException("A user with that name already exists: " + registerRequest.getUsername());
        }
        if (appUserRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("A user with that email already exists: " + registerRequest.getEmail());
        }
        AppUser user = new AppUser();
        user.setUsername(registerRequest.getUsername());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName(RoleNames.USER)
                .orElseThrow(() -> new RuntimeException("Role USER not found"))));
        user.setCreatedAt(OffsetDateTime.now());
        appUserRepository.save(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return new ResponseEntity<>(new TokenResponse(token), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()
            ));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid login or password");
        }
        AppUser user = appUserRepository.getUserByUsername((loginRequest.getUsername()));
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
