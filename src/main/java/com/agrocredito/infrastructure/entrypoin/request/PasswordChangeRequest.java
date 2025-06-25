package com.agrocredito.infrastructure.entrypoin.request;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private Integer id;
    private String oldPassword;
    private String newPassword;
}
