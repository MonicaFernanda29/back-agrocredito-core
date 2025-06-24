package com.agrocredito.infrastructure.entrypoin.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String telefono;
    private String ciudad;
    private String direccion;
    private String rol;
}
