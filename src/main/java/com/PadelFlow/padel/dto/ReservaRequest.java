package com.PadelFlow.padel.dto;

import lombok.Data;

@Data
public class ReservaRequest {
    private Long pistaId;
    private String fecha;  // formato: "2026-04-15"
    private String hora;   // formato: "09:00"
}