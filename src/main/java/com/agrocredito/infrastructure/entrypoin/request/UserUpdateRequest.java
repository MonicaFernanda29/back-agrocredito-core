package com.agrocredito.infrastructure.entrypoin.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Integer id;
    private String name;
    private String email;
    private String telefono;
    private String ciudad;
    private String direccion;
}
