package com.agrocredito.infrastructure.entrypoin.auth;

import com.agrocredito.infrastructure.drivenadapter.authadapter.AuthService;
import com.agrocredito.infrastructure.entrypoin.request.AuthRequest;
import com.agrocredito.infrastructure.entrypoin.request.RegisterRequest;
import com.agrocredito.infrastructure.entrypoin.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("protected/auth/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthRest {

    private final AuthService service;

    @PostMapping("register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        final TokenResponse response = service.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
        final TokenResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("refresh-token")
    public TokenResponse refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication
    ) {
        return service.refreshToken(authentication);
    }
}
