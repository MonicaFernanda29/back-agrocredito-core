package com.agrocredito.infrastructure.entrypoin.auth;

import com.agrocredito.domain.model.entity.user.UserEntity;
import com.agrocredito.domain.model.repository.user.UserRepository;
import com.agrocredito.infrastructure.entrypoin.request.PasswordChangeRequest;
import com.agrocredito.infrastructure.entrypoin.request.UserUpdateRequest;
import com.agrocredito.infrastructure.entrypoin.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
public class UserRest {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Listar todos los usuarios
    @GetMapping("list")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserResponse.builder()
                        .email(user.getEmail())
                        .name(user.getName())
                        .build())
                .toList();
    }

    // Actualizar perfil
    @PostMapping("update")
    public ResponseEntity<Boolean> updateProfile(@RequestBody UserUpdateRequest request) {
        if(Objects.nonNull(request)){
                userRepository.findById(request.getId())
                       .ifPresent(userEntity ->{
                            UserEntity updatedUser = userEntity.toBuilder()
                                    .name(request.getName())
                                    .email(request.getEmail())
                                    .ciudad(request.getCiudad())
                                    .direccion(request.getDireccion())
                                    .telefono(request.getTelefono())
                                    .build();

                            userRepository.save(updatedUser);
                        });
        }

        return ResponseEntity.ok().body(true);
    }

    // Cambiar contraseña
    @PostMapping("password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        var user = userRepository.findById(request.getId()).orElseThrow();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("❌ Contraseña actual incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
