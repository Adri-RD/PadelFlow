package com.PadelFlow.padel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String nombre;
    private String email;
}