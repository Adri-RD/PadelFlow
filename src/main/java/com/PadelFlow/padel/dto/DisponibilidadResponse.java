package com.PadelFlow.padel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisponibilidadResponse {
    private String hora;
    private boolean libre;
    private Long miReservaId;  // null si no es tuya
}