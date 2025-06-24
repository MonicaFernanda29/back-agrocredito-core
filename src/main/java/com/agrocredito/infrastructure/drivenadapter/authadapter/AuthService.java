package com.agrocredito.infrastructure.drivenadapter.authadapter;


import com.agrocredito.domain.model.entity.auth.TokenEntity;
import com.agrocredito.domain.model.entity.user.UserEntity;
import com.agrocredito.domain.model.repository.auth.TokenRepository;
import com.agrocredito.domain.model.repository.user.UserRepository;
import com.agrocredito.infrastructure.entrypoin.request.AuthRequest;
import com.agrocredito.infrastructure.entrypoin.request.RegisterRequest;
import com.agrocredito.infrastructure.entrypoin.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(final RegisterRequest request) {
        final UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .telefono(request.getTelefono())
                .ciudad(request.getCiudad())
                .direccion(request.getDireccion())
                .rol(request.getRol() != null ? request.getRol() : "Cliente")
                .build();

        final UserEntity savedUser = repository.save(user);
        final String jwtToken = jwtService.generateToken(savedUser);
        final String refreshToken = jwtService.generateRefreshToken(savedUser);

        this.saveUserToken(savedUser, jwtToken);
        return TokenResponse.builder().refreshToken(refreshToken).accessToken(jwtToken).build();
    }

    public TokenResponse authenticate(final AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        final UserEntity user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        final String accessToken = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);
        this.revokeAllUserTokens(user);
        this.saveUserToken(user, accessToken);
        return TokenResponse.builder().refreshToken(refreshToken).accessToken(accessToken).build();
    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        final TokenEntity token = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenEntity.TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(final UserEntity user) {
        final List<TokenEntity> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setIsExpired(true);
                token.setIsRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public TokenResponse refreshToken(@NotNull final String authentication) {

        if (authentication == null || !authentication.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid auth header");
        }
        final String refreshToken = authentication.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            return null;
        }

        final UserEntity user = this.repository.findByEmail(userEmail).orElseThrow();
        final boolean isTokenValid = jwtService.isTokenValid(refreshToken, user);
        if (!isTokenValid) {
            return null;
        }

        final String accessToken = jwtService.generateRefreshToken(user);
        this.revokeAllUserTokens(user);
        this.saveUserToken(user, accessToken);

        return TokenResponse.builder().refreshToken(refreshToken).accessToken(accessToken).build();
    }
}