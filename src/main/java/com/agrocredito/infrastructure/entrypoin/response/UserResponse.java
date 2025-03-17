package com.agrocredito.infrastructure.entrypoin.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String name;
    private String email;
}
